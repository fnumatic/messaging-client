(ns cljs-reframe-template.use-cases.core-cases
  (:require
   [re-frame.core :as rf]
   [cljs-reframe-template.db :as db]
   [tools.reframetools :refer [sdb gdb]]))
   ;[day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(defn change-type [db [_ type]]
  (assoc-in db [:conversation :reply?] (= type :reply)))

(rf/reg-sub ::name (gdb [:name]))
(rf/reg-sub ::active-panel (gdb [:active-panel]))
(rf/reg-sub ::re-pressed-example  (gdb [:re-pressed-example]))
(rf/reg-sub :stream/main (gdb [:stream]))
(rf/reg-sub :stream/current (gdb [:stream :current]))
(rf/reg-sub :inbox/main (gdb [:inbox]))
(rf/reg-sub :sidebar (gdb [:sidebar]))
;(rf/reg-sub :conversation/main (gdb [:conversation]))
(rf/reg-sub :conversation-detail/main (gdb [:conversation-detail]))
(rf/reg-sub :conversations (gdb [:conversations]))


(rf/reg-sub :conversation/main
  :<- [:stream/current]
  :<- [:conversations] 
   (fn [[current conversations] _]
     (get conversations current)))

(rf/reg-event-db :conversation/update-msg (sdb [:conversation :msg]))
(rf/reg-event-db :conversation/update-note (sdb [:conversation :note]))
(rf/reg-event-db :conversation/change-type change-type)

(rf/reg-event-db ::initialize-db (constantly db/default-db))
(rf/reg-event-db ::set-active-panel [rf/debug] (sdb [:active-panel]))

(rf/reg-event-db :stream/set-current (sdb [:stream :current]))
(rf/reg-event-db :sidebar/set-active (sdb [:sidebar :active1]))



