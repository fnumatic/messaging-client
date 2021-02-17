(ns cljs-reframe-template.use-cases.core-cases
  (:require
   [re-frame.core :as rf]
   [cljs-reframe-template.db :as db]
   [tools.reframetools :refer [sdb gdb]]))
   ;[day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))



(rf/reg-sub ::name (gdb [:name]))
(rf/reg-sub ::active-panel (gdb [:active-panel]))
(rf/reg-sub ::re-pressed-example  (gdb [:re-pressed-example]))
(rf/reg-sub :stream/get-active (gdb [:active-stream]))
(rf/reg-sub :stream/items (gdb [:stream]))
(rf/reg-sub :inbox/items (gdb [:inbox-items]))
(rf/reg-sub :sidebar (gdb [:sidebar]))
(rf/reg-sub :conversation/main (gdb [:conversation]))
(rf/reg-sub :conversation-detail/main (gdb [:conversation-detail]))


(rf/reg-event-db :conversation/update-msg (sdb [:conversation :msg]))
(rf/reg-event-db ::initialize-db (constantly db/default-db))
(rf/reg-event-db ::set-active-panel [rf/debug] (sdb [:active-panel]))

(rf/reg-event-db :stream/set-active (sdb [:active-stream]))
(rf/reg-event-db :sidebar/set-active (sdb [:sidebar :active1]))



