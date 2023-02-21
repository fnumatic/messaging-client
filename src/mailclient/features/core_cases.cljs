(ns mailclientfeatures.core-cases
  (:require
   [re-frame.core :as rf :refer [unwrap]]
   [mailclient.db :as db]
   [mailclient.svg :as v]
   ;;[meander.epsilon :as m]
   [ribelo.doxa :as dx]
   [tools.doxatools :as dxt]
   ;;[tick.alpha.api :as tck]
   [tick.core :as tck]
   [tools.reframetools :refer [sdb gdb sdbj sdbx tudb dispatch-n]]
   [medley.core :refer [assoc-some]]
   [debux.cs.core :as d :refer-macros [dbg dbgn break]]))

   ;[day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))



(def current-conv
  [:conversations [:stream :current]])

(defn reply-type? [[_ type]]
  (= type :reply))

(defn message [msg]
  {:icon v/user-circle :msg msg :time "1min ago" :me true})

(defn append-msg [messages msg]
  (conj messages (message msg)))

(defn header [{:keys [person/id name]}]
  {:id id
   :person name
   :action {:icon v/user-circle}
   :title ""})

(rf/reg-sub ::active-panel (gdb [:active-panel]))
(rf/reg-sub :stream/current (gdb [:stream :current]))
(rf/reg-sub :inbox (gdb [:inbox]))
(rf/reg-sub :doxa (gdb [:db]))


(rf/reg-sub :sidebar (gdb [:sidebar]))
(rf/reg-sub :conversation-detail/main (gdb [:conversation-detail]))
(rf/reg-sub :conversations (gdb [:conversations]))

(defn enrich-current-entity [current idfn e]
  (assoc-some e :current (= current (idfn e))))

(defn q-sb [db_ sidebar-id active-sidebar]
  (->>
   (dxt/query-pull-s db_
                      `[:* {:icon [:value]}]
                      `[:find [?e ...]
                        :where [?e :sb ~sidebar-id]])
   (map (partial enrich-current-entity active-sidebar :sb-item/id))))

(defn q-all-streams [db_]
  (dxt/query-pull (dx/table db_ :person/id)
                  `[:person/id :inbox :short :name :block-msg :time]
                  `[:find    [?e ...]
                    :where  [?e :person/id]]))
(defn q-stream_ [db_ inbox]
  (dx/q
   `[:find    [?e ...]
     :where [?e :inbox ~inbox] [?e :person/id]]
   (dx/table db_ :person/id)))

(defn q-stream [db_ inbox]
  (->>
   (dxt/query-pull (dx/table db_ :person/id)
                   `[:person/id :inbox :short :name :block-msg :time]
                    `[:find    [?e ...]
                      :where [?e :inbox ~inbox] [?e :person/id]])))
   

(defn all-inbox? [inbox]
  (= inbox 3))

(defn enrich-inbox-count [db inbox]
  (let [q-fn (if (all-inbox? (:inbox/id inbox))
               #(q-all-streams db)
               #(q-stream_ db %))]
   (->> (:inbox/id inbox)
       (q-fn)
       count 
       (assoc inbox :count))))

(defn q-inbox [db_]
  (->> (dxt/query-pull-s   db_ [:* {:icon [:value]}] (dxt/q-entity :inbox/id))
       (map (partial enrich-inbox-count db_))))

(defn p-person-conversation [doxa stream]
  (dx/pull doxa [:* {:messages [:* {:icon [:*]}]}] [:person/id stream]))


(defn enrich-duration [e]
  (update e :time #(as-> (tck/date-time %) d
                     (tck/between d (tck/now))
                     (tck/units d)
                     (cond
                       (not= (:years d) 0) (str (:years d) "yr")
                       (not= (:months d) 0) (str (:months d) "mth")
                       (not= (:days d) 0) (str (:days d) "d")
                       :else (str d)))))

(rf/reg-sub :sidebar/main
            :<- [:sidebar]
            :<- [:doxa]
            (fn [[sidebar doxa]]
              {:sidebar1 (q-sb doxa 1 (:active1 sidebar))
               :sidebar2 (q-sb doxa 2 nil)}))

(rf/reg-sub :inbox/main
            :<- [:inbox]
            :<- [:doxa]
            (fn [[inbox doxa] _]
              {:items   (q-inbox doxa)
               :current (:current inbox)}))

(defn q-stream2 [inbox db]
 (if (all-inbox? inbox)
   (q-all-streams db)
   (q-stream db inbox)))

(rf/reg-sub :stream/main
            :<- [:stream/current]
            :<- [:inbox]
            :<- [:doxa]
            (fn [[stream inbox doxa] _]
              (let [current-inbox (:current inbox)
                    stream (get stream current-inbox)
                    pers (q-stream2 current-inbox doxa)
                    firstp (-> pers first :person/id)
                    enrich-current  (partial enrich-current-entity (or  stream firstp) :person/id)]
                   
                {:items       (map  (comp enrich-duration enrich-current) pers)
                 :current     (or stream firstp)
                 :count       (count pers)
                 :stream-name (-> (dx/pull doxa [:name] [:inbox/id current-inbox]) :name)
                 :inbox       current-inbox})))

(rf/reg-sub :conversation/main
            :<- [:stream/main]
            :<- [:doxa]
            (fn [[stream doxa] _]
              (p-person-conversation doxa (:current stream))))

(rf/reg-sub :conversation/editor
            :<- [:conversation/main]
            :<- [:conversations]
            (fn [[conversation conversations] _]
              (merge
               conversations
               (dissoc conversation :messages :short :name :time  :block-msg))))

(rf/reg-sub :conversation/header
            :<- [:conversation/main]
            (fn [d]
              (header d)))

(defn- time-stamp []
  (.toLocaleTimeString (js/Date.)))

(defn enrich-time [db [_ {:keys [time]}]]
  (if time
    (assoc-in db (conj db/msg-path :time) (time-stamp))
    db))

(defn update-note [db {:keys [id data]}]
  (update db :db
          #(dx/commit % [[:dx/update [:person/id id] assoc :note data]])))

(defn update-msg [db {:keys [id data]}]
  (update db :db
          #(dx/commit % [[:dx/update [:person/id id] assoc :reply-msg data]])))

(rf/reg-event-db :msg/log [(rf/enrich enrich-time)] (sdb db/msg-path))
(rf/reg-event-db :conversation/update-msg [unwrap] update-msg)
(rf/reg-event-db :conversation/update-note [unwrap] update-note)
(rf/reg-event-db :conversation/change-type (sdb [:conversations :reply?]))
(rf/reg-event-db :conversation/change-title (sdbj (conj current-conv :header :title)))
(rf/reg-event-db :conversation/send-msg (tudb  (conj current-conv :msg)
                                               (conj current-conv :items)
                                               append-msg))

(rf/reg-event-fx :conversation/send-msg-flow (dispatch-n [:conversation/send-msg]
                                                         [:conversation/update-msg ""]
                                                         [:msg/log "msg send success"]))


(rf/reg-event-db ::initialize-db (constantly db/default-db))
(rf/reg-event-db ::set-active-panel (sdb [:active-panel]))

(rf/reg-event-db :stream/set-current  (sdbx [:stream :current (comp first second)] (comp second second)))
(rf/reg-event-db :sidebar/set-active (sdb [:sidebar :active1]))
(rf/reg-event-db :inbox/set-active (sdb [:inbox :current]))

(comment
  
 
  (defn q-sb3_ [db_ sidebar]
    (dxt/query-pull db_ 
                `[:* {:icon [:value]}]
                `[:find   ?e 
                  :where [?e :sb ~sidebar]]))) 


(comment
  (tap>   @re-frame.db/app-db)
  (dx/commit {} [[:dx/update [:person/id 1] assoc :aka "Tupen"]])
  (q-sb  (:db @re-frame.db/app-db) 1 1)
  (q-sb3_  (:db @re-frame.db/app-db) 1)
  (sort-by second
   (dx/q 
     `[:find    [?e ...]
       :where [?e :sb 1]]
     (:db @re-frame.db/app-db)))
  (dx/q
   `[:find  ?e   ?f
     :where [?e :inbox/id] [?p_ :person/id ?f]] 
      
   (:db @re-frame.db/app-db)
   (dx/q
    `[:find  (count ?e) 
      :where [?e :inbox/id]]
    (:db @re-frame.db/app-db)))  
  ;dx/datalog->meander
  (q-inbox (:db @re-frame.db/app-db))
  (if nil :a :b)
  ,)


(defn wrap [el txt]
 (str "<" el ">" txt "</" el ">"))

(def th (partial wrap "th"))

(th "foobar")