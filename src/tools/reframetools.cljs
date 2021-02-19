(ns tools.reframetools)

(defn sdb [path]
  (fn [db [_ v]]
    (assoc-in db path v)))

(defn sdbx [path fnv]
  (fn [db param]
    (let [repath #(if (fn? %) (% param) %)]
     (assoc-in db (mapv repath path) (fnv param)))))

(defn gdb
  [path]
  (fn [db _] (get-in db path)))