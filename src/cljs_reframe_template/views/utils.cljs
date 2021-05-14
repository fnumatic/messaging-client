(ns cljs-reframe-template.views.utils)


(defn spread-by-id [compo coll & opts ]
  (let [enrich-item #(if opts (merge % (first opts)) %)]
   (for [item coll]
     ^{:key (:id item)}
     [compo (enrich-item item)])))

(defn spread-by-id2 [compo coll idfn & opts]
  (let [enrich-item #(if opts (merge % (first opts)) %)]
    (for [item coll]
      ^{:key (idfn item)}
      [compo (enrich-item item)])))

(defn spread-by-order [compo coll & opts]
  (let [enrich-item #(if opts (merge % (first opts)) %)]
   (for [[idx item] (map-indexed vector coll)]
    ^{:key idx} [compo (enrich-item item)])))

(defn target-value [evt]
  (-> evt .-target .-value)) 