(ns cljs-reframe-template.use-cases.core-cases
  (:require
   [re-frame.core :as rf]
   [cljs-reframe-template.db :as db]
   [cljs-reframe-template.svg :as v]
   [tools.reframetools :refer [sdb gdb sdbx sdbj repathv]]))
   ;[day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(defn reply-type? [[_ type]]
  (= type :reply))

(defn message [msg]
  {:icon v/user-circle :msg msg :time "1min ago" :me true})

(defn send-msg [db _]
  (let [msg (get-in db (repathv db [:conversations [:stream :current] :msg])) ]
   (update-in db (repathv db [:conversations [:stream :current] :items])
              #(conj % (message msg)))))

(rf/reg-sub ::name (gdb [:name]))
(rf/reg-sub ::active-panel (gdb [:active-panel]))
(rf/reg-sub ::re-pressed-example  (gdb [:re-pressed-example]))
(rf/reg-sub :stream/main (gdb [:stream]))
(rf/reg-sub :stream/current (gdb [:stream :current]))
(rf/reg-sub :inbox/main (gdb [:inbox]))
(rf/reg-sub :sidebar (gdb [:sidebar]))
(rf/reg-sub :conversation-detail/main (gdb [:conversation-detail]))
(rf/reg-sub :conversations (gdb [:conversations]))


(rf/reg-sub :conversation/main
  :<- [:stream/current]
  :<- [:conversations] 
   (fn [[current conversations] _]
     (get conversations current)))

(rf/reg-sub :conversation/header
   :<- [:conversation/main] 
   (fn [d]
     (:header d)) )

(rf/reg-event-db :conversation/update-msg  (sdbj [:conversations [:stream :current] :msg]))
(rf/reg-event-db :conversation/update-note (sdbj [:conversations [:stream :current] :note]))
(rf/reg-event-db :conversation/change-type (sdbj [:conversations [:stream :current] :reply?] reply-type?))
(rf/reg-event-db :conversation/change-title (sdbj [:conversations [:stream :current] :header :title]))
(rf/reg-event-db :conversation/send-msg send-msg)

(rf/reg-event-db ::initialize-db (constantly db/default-db))
(rf/reg-event-db ::set-active-panel [rf/debug] (sdb [:active-panel]))

(rf/reg-event-db :stream/set-current (sdb [:stream :current]))
(rf/reg-event-db :sidebar/set-active (sdb [:sidebar :active1]))



