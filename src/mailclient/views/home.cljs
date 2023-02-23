(ns mailclient.views.home
  {:clj-kondo/config '{:lint-as {reagent.core/with-let clojure.core/let}}}
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r ]
   [mailclient.features.core-cases :as ccases]
   [mailclient.views.conversation :as cnv]
   [mailclient.views.inbox :as inbx]
   [mailclient.views.stream :as strm]
   [mailclient.views.sidebar :as sdb]
   [integrant.core :as ig]
   [tools.tailwindtools :refer [twl]]
   [mailclient.views.tw-classes :refer [ main-css]]
   [cljs.pprint :as pp]))


(defn component [{:keys [defaults on-mount state render ui]}]
  (fn [options]
    (r/with-let [_ (when on-mount (on-mount))]
      [render (merge (when state @state)
                     defaults
                     options)])))


(defmethod ig/init-key :default [_ config]
  (component config))
;; main

(defn show-panel [route]
  (when-let [route-data (:data route)]
    (let [view (:view route-data)]
      [:<>
       [view]])))


(defn main-panel []
  (let [active-route (rf/subscribe [::ccases/active-panel])]
    [show-panel @active-route]))

(def pageconfig
  {::sidebar              {:defaults {:activate-view #(rf/dispatch [:sidebar/set-active  %])}
                           :state (rf/subscribe [:sidebar/main]) 
                           :render   sdb/sidebar}
   ::inbox                {:defaults {:change-current #(rf/dispatch [:inbox/set-active %])}
                           :state    (rf/subscribe [:inbox/main])
                           :render   inbx/inbox}
   ::you-stream           {:defaults {:change-current #(rf/dispatch [:stream/set-current %])}
                           :state    (rf/subscribe [:stream/main])
                           :render   strm/you-stream}
   ::conversation-editor  {:defaults {:update-msg #(rf/dispatch [:conversation/update-msg {:id %1 :data %2}])
                                      :send-msg  #(rf/dispatch [:conversation/send-msg-flow])
                                      :update-note (fn [id txt](rf/dispatch [:conversation/update-note {:id id :data txt}]))
                                      :save-note #(println "note saved")
                                      :change-type #(rf/dispatch [:conversation/change-type %])}
                           :state    (rf/subscribe [:conversation/editor])
                           :render   cnv/conversation-editor}
   ::conversation-header  {:defaults {:change-title #(rf/dispatch [:conversation/change-title %])}
                           :state    (rf/subscribe [:conversation/header])
                           :render    cnv/conversation-header}
   ::conversation         {:defaults {:editor  (ig/ref ::conversation-editor)
                                      :header  (ig/ref ::conversation-header)}
                           :state (rf/subscribe [:conversation/main])
                           :render cnv/conversation}
   ::conversation-details {:state (rf/subscribe [:conversation-detail/main])
                           :render cnv/conversation-settings}})


(defn main-component [{:keys [::sidebar ::inbox ::you-stream ::conversation ::conversation-details]}]
  (let [{:main/keys [top content]} (twl main-css)]
    [:div top
     [sidebar]
     [inbox]
     [:div content
      [you-stream]
      [conversation]
      [conversation-details]]]))


(defn main []
  [main-component (ig/init pageconfig)])
