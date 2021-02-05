(ns cljs-reframe-template.views.utils)


(defn spread-by-id [comp coll ]
  (for [item coll]
    ^{:key (:id item)} [comp item]))

(defn spread-by-order [comp coll ]
  (for [[idx item] (map-indexed vector coll)]
    ^{:key idx} [comp item]))