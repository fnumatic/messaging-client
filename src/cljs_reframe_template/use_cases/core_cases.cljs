(ns cljs-reframe-template.use-cases.core-cases
  (:require
   [re-frame.core :as rf]
   [cljs-reframe-template.db :as db]
   [cljs-reframe-template.svg :as v]
   [tools.reframetools :refer [sdb gdb sdbj tudb dispatch-n]]))
   ;[day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(def current-conv
  [:conversations [:stream :current]])

(defn reply-type? [[_ type]]
  (= type :reply))

(defn message [msg]
  {:icon v/user-circle :msg msg :time "1min ago" :me true})

(defn append-msg [messages msg]
  (conj messages (message msg)))

(rf/reg-sub ::name (gdb [:name]))
(rf/reg-sub ::active-panel (gdb [:active-panel]))
(rf/reg-sub ::re-pressed-example  (gdb [:re-pressed-example]))
(rf/reg-sub :stream/current (gdb [:stream :current]))
(rf/reg-sub :inbox/main (gdb [:inbox]))
(rf/reg-sub :inbox/current (gdb [:inbox :current]))
(rf/reg-sub :sidebar (gdb [:sidebar]))
(rf/reg-sub :conversation-detail/main (gdb [:conversation-detail]))
(rf/reg-sub :conversations (gdb [:conversations]))

(defn same-inbox [ conv inbox-token   ]
  (= (:inbox conv) inbox-token))

(rf/reg-sub :stream/main
     :<- [:stream/current]  
     :<- [:conversations/main] 
    (fn [[stream conversations] _]
      {:current stream
       :items (map (comp :block second) conversations)}) )

(rf/reg-sub :conversations/main 
  :<- [:inbox/current]          
  :<- [:conversations]
   (fn [[inbox-token conversations] _]
     (->>
      conversations
      (filter #(same-inbox (second %) inbox-token))
      (into {})))         )


(rf/reg-sub :conversation/main
  :<- [:stream/current]
  :<- [:conversations/main] 
   (fn [[conversation-id conversations] _]
      (get conversations conversation-id)))

(rf/reg-sub :conversation/header
   :<- [:conversation/main] 
   (fn [d]
     (:header d)) )

(defn- time-stamp []
  (.toLocaleTimeString (js/Date.)))

(defn enrich-time [db [_ {:keys [time]}]]
  (if time
    (assoc-in db (conj db/msg-path :time) (time-stamp))
    db))

(rf/reg-event-db :msg/log [(rf/enrich enrich-time)] (sdb db/msg-path))
(rf/reg-event-db :conversation/update-msg  (sdbj (conj current-conv :msg)))
(rf/reg-event-db :conversation/update-note (sdbj (conj current-conv :note) ))
(rf/reg-event-db :conversation/change-type (sdbj (conj current-conv :reply?)  reply-type?))
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
  (tap>   @re-frame.db/app-db)
  ,)