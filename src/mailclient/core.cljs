(ns ^:figwheel-hooks mailclient.core
  (:require 
            [mailclient.config :as config]
            [mailclient.features.core-cases :as ccases]
            [mailclient.routes :as routes]
            [mailclient.styles :as styl]
            [mailclient.views.home :as views]
            [goog.dom :as gdom]
            [react :as react]
            [re-frame.core :as re-frame]
            [reagent.dom.client :as rdc]
            ))



(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defonce root (rdc/create-root (gdom/getElement "app")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (styl/inject-trace-styles js/document)
  (rdc/render root [:> react/StrictMode {} [#'views/main-panel]])
  )

(defn ^:after-load re-render []
  (mount-root))

(defn ^:export init []
  (println "init again..")
  (re-frame/dispatch-sync [::ccases/initialize-db])
  (dev-setup)
  (routes/app-routes)

  (mount-root))

(defonce init-block (init))
