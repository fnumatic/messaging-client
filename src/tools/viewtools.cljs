(ns tools.viewtools
  (:require [reitit.frontend.easy :as rtfe]))


(defn item [e]
  (cond
     (fn? e) [e]
     (vector? e) e
     (string? e) [:h2 e]))

(defn panel [name component]
  [:div

   [item name]
   [item component]])

;; navigation tools
(defn sep []
  [:span " | "])

(defn nav-item [i]
  (if (= :sep i)
    [sep]
    [:a.text-blue-700
     {:href (rtfe/href (second i))} (first i)]))

(defn navigation [routes]
  (let [coll (->> routes (interpose :sep) (map-indexed vector))]
    [:div
     (for [[idx rt]  coll]
        ^{:key (str idx)} [nav-item rt])]))


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