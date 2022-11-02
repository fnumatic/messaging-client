(ns cljs-reframe-template.features.core-cases
  (:require
   [re-frame.core :as rf :refer [unwrap]]
   [cljs-reframe-template.db :as db]
   [cljs-reframe-template.svg :as v]
   ;;[meander.epsilon :as m]
   [ribelo.doxa :as dx]
   [tools.doxatools :as dxt]
   ;;[tick.alpha.api :as tck]
   [tick.core :as tck]
   [tools.reframetools :refer [sdb gdb sdbj tudb dispatch-n]]))
   ;[day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))


(defn dbg [body]
  (tap> body)
  body)

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

(rf/reg-sub ::name (gdb [:name]))
(rf/reg-sub ::active-panel (gdb [:active-panel]))
(rf/reg-sub ::re-pressed-example  (gdb [:re-pressed-example]))
(rf/reg-sub :stream/current (gdb [:stream :current]))
(rf/reg-sub :streamm (gdb [:stream]))
(rf/reg-sub :inbox (gdb [:inbox]))
(rf/reg-sub :doxa (gdb [:db]))


(rf/reg-sub :inbox/current (gdb [:inbox :current]))
(rf/reg-sub :sidebar (gdb [:sidebar]))
(rf/reg-sub :conversation-detail/main (gdb [:conversation-detail]))
(rf/reg-sub :conversations (gdb [:conversations]))


(defn q-sb [db_ sidebar]
  (dxt/query-pull-s db_
   `[:* {:icon [:value]}]
   `[:find [?e ...]
     :where [?e :sb ~sidebar]]))

(defn q-stream [db_ inbox]
  (dxt/query-pull db_
   `[:person/id :inbox :short :name :block-msg :time]
   `[:find    [?e ...]
     :where [?e :inbox ~inbox] [?e :person/id]]))

(defn p-person-conversation [doxa stream]
  (dx/pull doxa [:* {:messages [:* {:icon [:*]}]}] [:person/id stream]))


(defn enrich-current [current idfn e]
  (if (= current (idfn e))
    (assoc e :current true)
    e))

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
              (let [enrich (partial enrich-current (:active1 sidebar) :sb-item/id)]
                (-> {}
                    (assoc  :sidebar1
                            (map enrich (q-sb doxa 1)))
                    (assoc  :sidebar2 (q-sb doxa 2))))))

(rf/reg-sub :inbox/main
            :<- [:inbox]
            :<- [:doxa]
            (fn [[inbox doxa] _]
              {:items   (dxt/query-pull-s doxa [:* {:icon [:value]}] (dxt/q-entity :inbox/id))
                        
               :current (:current inbox)}))



(rf/reg-sub :stream/main
            :<- [:stream/current]
            :<- [:inbox]
            :<- [:doxa]
            (fn [[stream inbox doxa] _]
              (let [current-inbox (:current inbox)
                    pers (q-stream doxa current-inbox)
                    instream? (some #{stream} (map :person/id pers))
                    firstp (-> pers first :person/id)
                    enrich  (partial enrich-current stream :person/id)]
                {:items       (map (comp enrich-duration enrich) pers)
                 :current     (if instream?
                                (or stream firstp)
                                firstp)
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
(rf/reg-event-db ::set-active-panel [rf/debug] (sdb [:active-panel]))

(rf/reg-event-db :stream/set-current (sdb [:stream :current]))
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
  (q-sb  (:db @re-frame.db/app-db) 1)
  (q-sb3_  (:db @re-frame.db/app-db) 1)
  (sort-by second
   (dx/q 
     `[:find    [?e ...]
       :where [?e :sb 1]]
     (:db @re-frame.db/app-db))
   (dx/q
    `[:find [?e ...]
      :where [?e :sb-item/id]]
    (:db @re-frame.db/app-db)))  
  ;dx/datalog->meander
  
  (tap> (q-stream  (:db @re-frame.db/app-db) 1)))


(defn wrap [el txt]
 (str "<" el ">" txt "</" el ">"))

(def th (partial wrap "th"))

(th "foobar")