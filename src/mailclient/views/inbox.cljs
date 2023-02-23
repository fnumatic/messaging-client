(ns mailclient.views.inbox
  (:require [mailclient.svg :as v :refer [svg]]
            [mailclient.tools.reagent-hooks :refer [use-state]]
            [mailclient.views.tw-classes :refer [inbox-css]]
            [reagent.core :as r ]
            [tools.tailwindtools :refer [twl twon]]
            [tools.viewtools :refer [spread-by-id2]]))



(defn inbox-view [{:keys [inbox/id icon name count current change-current] }]
  (let [{:view/keys [top]} inbox-css
        {:view/keys [svgc namec countc]} (twl inbox-css)
        top (twon (= id current) [top :text-blue-700])]
    [:a (merge top
               {:on-click #(change-current id)})
     [svg svgc (:value icon)]
     [:div namec name]
     [:div countc count]]))

(defn inbox-view-expand [{:keys [msg action]}]
  (let [{:expand/keys [top msgc buttonc]} (twl inbox-css)]
    [:div top
     [:div msgc msg]
     [:button buttonc  action]]))

(defn inbox-action [{:keys [icon name]}]
  (let [{:action/keys [top svgc namec]} (twl inbox-css)]
    [:div top
     [svg svgc icon]
     [:div namec name]]))

(defn inbox-block [{:keys [title open?]} fragm]
  (r/with-let [{:block/keys [container]} inbox-css
               {:block/keys [top buttonc svgc]} (twl inbox-css)
               [open*? set-open] (use-state open?)]

    (let [icon   (if @open*? v/chevron-down v/chevron-left)
          container (twon (not @open*?) [container :hidden])]
      [:div top
       [:button (merge buttonc
                       {:on-click #(set-open (not @open*?))})
        title
        [svg svgc icon]]
       [:div container
        fragm]])))


(defn inbox [{:keys [items] :as opts}]
  (let [{:inbox/keys [top header h1c svgc]} (twl inbox-css)]
    [:div#inbox  top
     [:div header
      [:h1 h1c "Inbox"]
      [svg svgc v/search-icon]]
     [inbox-block {:title "Conversations" :open? true}
      [:<>
       (spread-by-id2 inbox-view items :inbox/id opts)
       [inbox-action {:icon v/plus :name "Create View"}]
       [inbox-view-expand {:msg "See 124 more" :action "Edit"}]]]
     [inbox-block {:title "Automation"}
      [:<>
       [inbox-action {:icon v/plus :name "Create Automation"}]
       [inbox-view-expand {:msg "See 42 more" :action "Edit"}]]]
     [inbox-block {:title "Your preferences"}]]))

