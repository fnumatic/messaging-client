(ns tools.reframetools)


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
     (let [repath #(if (vector? %) (get-in db %) %)]
       (assoc-in db (mapv repath path) (fnv param))))))