(ns cljs-reframe-template.db
  (:require [cljs-reframe-template.svg :as v]
            [ribelo.doxa :as dx]
            [nano-id.core :refer [nano-id]]
            [cljs-reframe-template.testdata :as td]))


(def msg-path [:ui :lastmsg])


(defn indexi-fy
  ([coll] (indexi-fy coll :id))
  ([coll k]
   (map-indexed
    #(assoc %2 k %1) coll)))

(defn nanoid-fy [coll]
  (mapv
   #(assoc % :message/id (nano-id)) coll))


(def conversation-stream
  [[0 "Nikola Tesla" "NT" "5hr" true]
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



(defn conversations [short]
  [{:icon short :msg "Some message text" :time "4hr ago"}
   {:type :hint :msg "You assigned this conversation to yourself 5d ago"}
   {:icon [[:icon/id :icon/user-circle]] :msg "Some message text" :time "5hr ago" :me true}
   {:icon [[:icon/id :icon/user-circle]] :msg "Some message text" :time "6hr ago" :me true}
   {:icon short :msg "Some message text" :time "6.5hr ago"}
   {:icon [[:icon/id :icon/user-circle]] :msg "Some message text" :time "7hr ago" :me true}])



(def streams2
  [{:icon  [[:icon/id :icon/user-circle]] :name "You" :count 5}
   {:icon [[:icon/id :icon/at-symbol]] :name "Mentions" :count 0}
   {:icon [[:icon/id :icon/user-circle]] :name "Unassigned" :count 2497}
   {:icon [[:icon/id :icon/users]] :name "All" :count 5171}])

(def sidebar-items1
  (->> 
   
   [{}
    {:icon [[:icon/id :icon/brief-case]] :count "5" :active? true}
    {:icon [[:icon/id :icon/paper-airplane]]}
    {:icon [[:icon/id :icon/users]]}
    {:icon [[:icon/id :icon/book-open]]}
    {:icon [[:icon/id :icon/chip]]}
    {:icon [[:icon/id :icon/chart-bar]]}]
   (map #(assoc % :sb 1))))
   

(def sidebar-items2
  (->>
   [{:icon [[:icon/id :icon/template]]}
    {:icon [[:icon/id :icon/bell]]}
    {:icon [[:icon/id :icon/user-circle]]}]
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

(defn conversation-template [[idx name short  time current inbox]]
  {:id idx
   :items (nanoid-fy (conversations short))
   :person name
   :block (conv-block-data [idx name short time current])
   :msg   (str "Hello " name)
   :note  "Note to myself"
   :reply? true
   :inbox (or inbox 0)})

(defn month-diff [d1 d2]
  (+ (- (.getMonth d2) (.getMonth d1))
     (* 12 (- (.getFullYear d2) (.getFullYear d1)))))

(defn conv-templ [{:keys [id name short time inbox]}]
  (conversation-template
   [id name short
    (str (month-diff (js/Date. time) (js/Date.)) "mth")
    false inbox]))

(defn conv-thread [inbox]
  (comp (juxt :id conv-templ) #(assoc % :inbox inbox) td/create-conv))

(def conversations-db
  (->> conversation-stream
       (map (juxt first conversation-template))
       (concat (map (conv-thread 2) (range 20 30)))
       (concat (map (conv-thread 3) (range 30 40)))
       (into {})))

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

(defn build-doxa []
  (-> {}
   (dx/db-with (map #(-> {:icon/id (first %) :value (second %)}) td/icons))
   (dx/db-with (indexi-fy streams2 :inbox/id))
   (dx/db-with (indexi-fy (concat sidebar-items1 sidebar-items2) :sb-item/id))
   (dx/db-with (mapv #(create-person-with-data 0) (range 0 10)))
   (dx/db-with (mapv #(create-person-with-data 1) (range 0 10)))
   (dx/db-with (mapv #(create-person-with-data 2) (range 0 10)))))

(def default-db
  
  {;;:icons               td/icons
   :stream              {:current nil}
   :inbox               {:current 0}
   :sidebar             {:active1  1}
   :conversations        conversations-db
   :conversation-detail {:items details-items}
   :db (build-doxa)})

(defn pull-ids [id db]
  (mapv #(-> [id %]) (keys (get db id))))
(def dummy
  (-> {}
      (dx/db-with (map #(-> {:icon/id (first %) :value (second %)}) td/icons))
      (dx/db-with (indexi-fy streams2 :inbox/id))))

(def icon-shape '[:name {:icon [:value]}])
(defn ddb [] (:db @re-frame.db/app-db))
#_:clj-kondo/ignore
(comment
   (tap> sidebar-items1)
  
  (tap>   @re-frame.db/app-db)
  dummy
  (dx/q [:find ?e
         :where [?e :count 2497]] dummy)
  ;;sidebar items
  (dx/q [:find [(pull [:name {:icon [:value]}] [:sb-item/id ?e]) ...]
         :where [?e :sb 2]] (ddb))
  
  ;;persons for stream
  (dx/q [:find [(pull [:inbox :short :name] [:person/id ?e]) ...]
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
  (take 3 conversations-db)
  (take-last 1 conversations-db)
  (dx/db-with {}
              (map  td/create-person (range 1 10)))

  (dx/db-with {}
              (map  create-message (range 10 20)))
  
  )
