(ns tools.tailwindtools)


(defn tw
  "nested coll of tw classes to :class map
   [:foo [:bar :buzz]] -> {:class [:foo :bar :buzz]}"
  [& classes]
  {:class (flatten (apply concat classes))})

(defn twon
  "tailwind on flag"
  [flag coll]
  (tw
   (if flag
     coll
     (first coll))))

(defn twl
  "all colls on mapvalues to :class maps
   {:foo [:bar]} -> {:foo {:class [:bar]}}"
  [tw-map]
  (reduce-kv
   #(assoc %1 %2 (tw %3))
   {}
   tw-map))