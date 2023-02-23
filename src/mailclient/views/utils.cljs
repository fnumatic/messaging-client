(ns mailclient.views.utils 
  (:require [mailclient.views.tw-classes :refer [component-css]]
            [tools.tailwindtools :refer [tw]]))




(defn text-icon [opts txt]
  (let [{:icon/keys [textc]} component-css]
    [:div (tw textc opts) txt]))
