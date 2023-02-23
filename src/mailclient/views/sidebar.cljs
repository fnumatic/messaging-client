(ns mailclient.views.sidebar
  (:require [mailclient.svg :as v :refer [svg]]
            [mailclient.views.tw-classes :refer [sidebar-css]]
            [mailclient.views.utils :as u :refer [text-icon]]
            [tools.tailwindtools :refer [twl twon]])
  )


(defn sidebar-item [{:keys [sb-item/id count icon activate-view current]}]
  (let [{:item/keys [small-overlay red-white top iconc]}  sidebar-css
        {:item/keys [nonicon]} (twl sidebar-css)
        top (twon current [top :bg-gray-100])
        iconc (twon current [iconc :text-blue-700])]
    [:a  (merge top
                {:href "#"}
                (when activate-view {:on-click #(activate-view id)}))
     (if icon
       [svg iconc (:value icon)]
       [:div nonicon])
     (when count
       [text-icon [small-overlay red-white] count])]))

(defn sidebar [{:keys [sidebar1 sidebar2] :as opts}]
  (let [{:main/keys [top container]} (twl sidebar-css)]
    [:div#sidebar top
     [:div container
      (u/spread-by-id2 sidebar-item sidebar1 :sb-item/id (dissoc opts :sidebar1 :sidebar2))]

     [:div container
      (u/spread-by-id2 sidebar-item sidebar2 :sb-item/id)]]))
