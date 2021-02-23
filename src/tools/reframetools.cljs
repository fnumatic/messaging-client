(ns tools.reframetools)

(defn repathv [db path]
  (let [rp #(if (vector? %) (get-in db %) %)]
    (mapv rp path)))

(defn gdb
  [path]
  (fn [db _] (get-in db path)))

(defn sdb [path]
  (fn [db [_ v]]
    (assoc-in db path v)))

(defn sdbx 
  "a fn inside path applied to param to build the real path. a fn applied to param as value"
  [path fnv]
  (fn [db param]
    (let [repath #(if (fn? %) (% param) %)]
     (assoc-in db (mapv repath path) (fnv param)))))

(defn sdbj 
  "a path inside path for a join. e.g. inject current id into path"
  ([path]
   (sdbj path second))
  ([path fnv]
   (fn [db param]
     (assoc-in db (repathv db path) (fnv param)))))

(defn tdb 
  "transport value from path to path, assoc with/without fn"
  [from to fnv]
  (fn [db _]
    (let [v (get-in db (repathv db from))]
     (assoc-in db (repathv db to) ((or fnv identity) v)))))

(defn tudb
  "transport value from path to path, update via fn"
  [from to fnv]
  (fn [db _]
    (let [v (get-in db (repathv db from))]
      (update-in db (repathv db to) #(fnv % v)))))

(defn dispatch-n
  [& events]
  (println {:fx (mapv #(-> [:dispatch %]) events)})
  (fn [_ _]
    {:fx (mapv #(-> [:dispatch %]) events )}))