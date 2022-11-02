(ns cljs-reframe-template.db
  (:require [cljs-reframe-template.testdata :as td]
            [nano-id.core :refer [nano-id]]
            re-frame.db
            [ribelo.doxa :as dx]))


(def msg-path [:ui :lastmsg])


(defn indexi-fy
  ([coll] (indexi-fy coll :id))
  ([coll k]
   (map-indexed
    #(assoc %2 k %1) coll)))

(defn nanoid-fy [coll]
  (mapv
   #(assoc % :message/id (nano-id)) coll))


(defn conversations [short]
  (shuffle
   [{:icon short :msg "Some message text" :time "4hr ago"}
    {:type :hint :msg "You assigned this conversation to yourself 5d ago"}
    {:icon [:icon/id :icon/user-circle] :msg "Some message text" :time "5hr ago" :me true}
    {:icon [:icon/id :icon/user-circle] :msg "Some message text" :time "6hr ago" :me true}
    {:icon short :msg "Some message text" :time "6.5hr ago"}
    {:icon [:icon/id :icon/user-circle] :msg "Some message text" :time "7hr ago" :me true}]))



(def streams2
  [{:icon [:icon/id :icon/user-circle] :name "You" :count 5}
   {:icon [:icon/id :icon/at-symbol] :name "Mentions" :count 0}
   {:icon [:icon/id :icon/user-circle] :name "Unassigned" :count 2497}
   {:icon [:icon/id :icon/users] :name "All" :count 5171}])

(def sidebar-items1
  (->>

   [{}
    {:icon [:icon/id :icon/brief-case] :count "5" :active? true}
    {:icon [:icon/id :icon/paper-airplane]}
    {:icon [:icon/id :icon/users]}
    {:icon [:icon/id :icon/book-open]}
    {:icon [:icon/id :icon/chip]}
    {:icon [:icon/id :icon/chart-bar]}]
   (map #(assoc % :sb 1))))

(def sidebar-items2
  (->>
   [{:icon [:icon/id :icon/template]}
    {:icon [:icon/id :icon/bell]}
    {:icon [:icon/id :icon/user-circle]}]
   (map #(assoc % :sb 2))))



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

#_(defn month-diff [d1 d2]
    (+ (- (.getMonth d2) (.getMonth d1))
       (* 12 (- (.getFullYear d2) (.getFullYear d1)))))


(defn create-message
  ([] (create-message (nano-id)))
  ([id]
   (->
    {:db/id id
     :db/type :messenger/message
     :icon "SL"
     :msg "some message"
     :time "5hr ago"})))

(defn create-messages [short]
  (nanoid-fy (conversations short)))

(defn create-person-with-data [inbox]
  (let [p (td/create-person)]
    (-> p
        (assoc :messages  (create-messages (:short p)))
        (assoc :inbox inbox))))

(def doxadb
  (concat
   (map #(-> {:icon/id (first %) :value (second %)}) td/icons)
   (indexi-fy streams2 :inbox/id)
   (indexi-fy (concat sidebar-items1 sidebar-items2) :sb-item/id)
   (map (fn [_] (create-person-with-data 0)) (range 0 10))
   (map (fn [_] (create-person-with-data 1)) (range 0 10))
   (map (fn [_] (create-person-with-data 2)) (range 0 10))))

(def default-db

  {;;:icons               td/icons
   :stream              {:current nil}
   :inbox               {:current 0}
   :sidebar             {:active1 1}
   :conversation-detail {:items details-items}
   :db (dx/create-dx {} doxadb)})

(defn pull-ids [id db]
  (mapv #(-> [id %]) (keys (get db id))))
(def dummy
  (-> {}
      (dx/create-dx (map #(-> {:icon/id (first %) :value (second %)}) td/icons))
      (dx/create-dx (indexi-fy streams2 :inbox/id))))

(def icon-shape '[:name {:icon [:value]}])
(defn ddb [] (:db @re-frame.db/app-db))
#_:clj-kondo/ignore
(comment
  (tap> sidebar-items1)

  (tap>   @re-frame.db/app-db)
  (tap> (ddb))
  dummy
  (dx/q '[:find ?e
          :where [?e :count 2497]] dummy)
  ;;sidebar items
  (dx/q '[:find [(pull [:name {:icon [:value]}] [:sb-item/id ?e]) ...]
          :where [?e :sb 2]] (ddb))

  ;;persons for stream
  (dx/q '[:find [(pull [:inbox :short :name] [:person/id ?e]) ...]
          :where [?e :inbox 0] [?e :person/id]] (ddb))
  ;;specific inbox
  (dx/pull (ddb) [:name] [:inbox/id 0])

  (dx/pull (ddb) {[:icon/id :icon/brief-case] [:*]})
  (dx/pull (ddb) [:*]  (conj (mapv #(-> [:sb-item/id %]) [0 1 7 8]) [:sb 2]))

  (dx/pull (ddb)  [:value]  (pull-ids :icon/id (ddb)))
  (dx/pull dummy icon-shape [:inbox/id 0])
  (dx/pull (ddb)  [:value] [[:icon/id] ...])
  (nano-id)
  (dx/haul dummy :inbox/id)
  (dx/commit {} [[:dx/put {:db/id 1 :name "foo"}]])
  (dx/create-dx {}
                (map  td/create-person (range 1 10)))

  (dx/create-dx {}
                (map  create-message (range 10 20))))


