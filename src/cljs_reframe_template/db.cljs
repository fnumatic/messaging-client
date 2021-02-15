(ns cljs-reframe-template.db
  (:require [cljs-reframe-template.svg :as v]))

(def default-db
  {:name "re-frame"})

(defn indexi-fy [coll]
  (map-indexed
   #(assoc %2 :id %1) coll))

(def nt  "Nikola Tesla")


(def conversation-block-data
  {:person nt
   :person-short "NT"
   :time   "5hr"
   :msg    "some message content whedkjwhed wkjehdkjweh dkjhwekjdhwekjhd "})

(def cbd-list
  (concat
   (repeat 2 conversation-block-data)
   [(merge conversation-block-data {:current true})]
   (repeat 3 conversation-block-data)))

(def conversations
  [{:icon v/user-circle :msg "Some message text" :time "4hr ago"}
   {:type :hint :msg "You assigned this conversation to yourself 5d ago"}
   {:icon v/user-circle :msg "Some message text" :time "5hr ago" :me true}
   {:icon v/user-circle :msg "Some message text" :time "6hr ago" :me true}
   {:icon v/user-circle :msg "Some message text" :time "6.5hr ago"}
   {:icon v/user-circle :msg "Some message text" :time "7hr ago" :me true}])

(def conversation-views
  [{:icon v/user-circle :name "You" :count 5}
   {:icon v/at-symbol :name "Mentions" :count 0}
   {:icon v/user-circle :name "Unassigned" :count 2497}
   {:icon v/users :name "All" :count 5171}])

(def sidebar-items1
  [{}
   {:icon v/brief-case :count "5" :active? true}
   {:icon v/paper-airplane}
   {:icon v/users}
   {:icon v/book-open}
   {:icon v/chip}
   {:icon v/chart-bar}])

(def sidebar-items2
  [{:icon v/template}
   {:icon v/bell}
   {:icon v/user-circle}])

(def details-items
  [["Bug Area" "Add"]
   ["Category" "Add"]
   ["Gravity Score" "Add"]
   ["Number attribute" "Add"]
   ["Product" "Add"]
   ["Rating" "Add"]
   ["Test type" "Add"]
   ["Topic" "Add"]
   ["Urgency" "Add"]
   ["Brand" "S&T"]
   ["ID" "333222333"]])

(def data
  {:conversations (indexi-fy conversations)
   :cbd-list (indexi-fy cbd-list)
   :conversation-views (indexi-fy conversation-views)
   :sidebar-items1 sidebar-items1
   :sidebar-items2 sidebar-items2
   :details-items details-items
   })