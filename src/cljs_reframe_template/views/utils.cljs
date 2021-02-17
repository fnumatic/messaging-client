(ns cljs-reframe-template.views.utils)


(defn spread-by-id [comp coll & opts ]
  (let [enrich-item #(if opts (merge % (first opts)) %)]
   (for [item coll]
     ^{:key (:id item)}
     [comp (enrich-item item)])))

(defn spread-by-order [comp coll & opts]
  (let [enrich-item #(if opts (merge % (first opts)) %)]
   (for [[idx item] (map-indexed vector coll)]
    ^{:key idx} [comp (enrich-item item)])))

(defn target-value [evt]
  (-> evt .-target .-value))