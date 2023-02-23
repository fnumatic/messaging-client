(ns mailclient.views.stream
  (:require [mailclient.svg :as v :refer [svg]]
            [mailclient.views.tw-classes :refer [def-texticon stream-css]]
            [mailclient.views.utils :as u :refer [text-icon]]
            [tools.tailwindtools :refer [tw twl]]
            [tools.viewtools :refer [spread-by-id2]])
  )



(defn stream-block [{:keys [person/id name short  time block-msg change-current current inbox]}]
  (let [{:block/keys [crnt nocrnt block]} stream-css
        {:block/keys [top container svg-small personc timec msgc]} (twl stream-css)
        blockcss (if current [block crnt] [block nocrnt])]
    [:a.conversation-block  
     (merge top
            {:on-click #(change-current [inbox id])})
     [:div (tw blockcss)
      [:div container
       [text-icon def-texticon short]
       [:strong personc name]
       [:div timec time]]
      [:div container
       [svg svg-small v/user-circle]
       [:div msgc block-msg]]]]))

(defn stream-header [{:keys [icon name]}]
  (let [{:header/keys [top svgc h1]} (twl stream-css)]
    [:div top
     [svg svgc icon]
     [:h1 h1 name]]))

(defn stream-menu [count]
  (let [{:menu/keys [top container]} (twl stream-css)]
    [:div top
     [:div container
      [svg {} v/brief-case]
      [:div count]
      [svg {} v/chevron-down]]
     [:div container
      [:div "newest"]
      [svg {} v/chevron-down]]]))

(defn you-stream [{:keys [items stream-name count] :as opts}]
  (let [{:you/keys [top blocks]} (twl stream-css)]
    [:div#you-stream top
     [stream-header {:icon v/menu-alt-1 :name stream-name}]
     [stream-menu count]
     [:div blocks
      (spread-by-id2 stream-block items :person/id (select-keys opts [:change-current :inbox]))]]))
