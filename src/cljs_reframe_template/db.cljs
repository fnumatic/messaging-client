(ns cljs-reframe-template.db
  (:require [cljs-reframe-template.svg :as v]))



(defn indexi-fy [coll]
  (map-indexed
   #(assoc %2 :id %1) coll))

(def nt  "Nikola Tesla")


(def conversation-stream
  [
   [0 nt "NT" "5hr" true]
   [1 "Fu Lan" "FL" "2mth"]
   [2 "natalia.alvarez@localhost" "N" "2mth"]
   [3 "aha" "A" "2mth"]
   [4 "Nikola and 30 others" "NT" "2mth"]])
   

(defn conv-block-data [[idx pers short time current]]
  (cond-> {:id idx
           :person       pers
           :person-short short
           :time         time
           :msg          "some message content whedkjwhed wkjehdkjweh dkjhwekjdhwekjhd "}
       current      (assoc :current true)))

(def cbd-list
  (map conv-block-data conversation-stream))

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

(def conversation-header-actions
  [{:icon v/dots-vertical}
   {:icon v/user-circle}
   {:icon v/star}
   {:icon v/clock}
   {:icon v/check}])

(defn conversation-template [[idx name]]
  {:id idx
   :items (indexi-fy conversations)
   :header {:person name
            :actions conversation-header-actions 
            :title ""}
   :msg   (str "Hello " name)
   :note  "Note to myself"
   :reply? true})

(def conversations-db
  (->> conversation-stream
       (map (juxt first conversation-template))
       (into {})))
  

(def default-db
  {:name                "re-frame"
   :stream              {:items  (indexi-fy cbd-list)
                         :current 0}
   :inbox               {:items (indexi-fy conversation-views)}
   :sidebar             {:sidebar1 (indexi-fy sidebar-items1)
                         :sidebar2 (indexi-fy sidebar-items2)
                         :active1  1}
   :conversations        conversations-db
   :conversation-detail {:items details-items}})