(ns cljs-reframe-template.views.home
  {:clj-kondo/config '{:lint-as {reagent.core/with-let clojure.core/let}}}
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [cljs-reframe-template.tools.reagent-hooks :refer [use-state]]
   [cljs-reframe-template.use-cases.core-cases :as ccases]
   [cljs-reframe-template.svg :as v :refer [svg]]
   [cljs-reframe-template.db :as db]
   [cljs-reframe-template.views.utils :as u]
   [integrant.core :as ig]
   [tools.viewtools :as vt]
   [cljs.pprint :as pp]
   [cljs-reframe-template.views.compo :as compo]))


(defn component [{:keys [defaults on-mount state render ui]}]
  (fn [options]
    (r/with-let [_ (when on-mount (on-mount))]
      [render (merge (when state @state) 
                     defaults 
                     options)]))) 
                     

(defmethod ig/init-key :default [_ config]
  (component config))

(def toolbar-items
  [
   ["#" :routes/frontpage]
   ["component" :routes/component]])

(defn route-info [route]
  [:div.m-4
   [:p "Routeinfo"]
   [:pre.border-solid.border-2.rounded
    (with-out-str (pp/pprint route))]])
;; main

(defn show-panel [route]
  (when-let [route-data (:data route)]
    (let [view (:view route-data)]
      [:<>
       [view]])))
       

(defn main-panel []
  (let [active-route (re-frame/subscribe [::ccases/active-panel])]
    [show-panel @active-route]))


(def conversation-background-img
  "url(https://static.intercomassets.com/ember/assets/images/messenger-backgrounds/background-1-99a36524645be823aabcd0e673cb47f8.png)")

(def justify-center  " justify-between items-center ")
(def flex-row  " flex flex-row ")
(def flex-col  " flex flex-col ")
(def center-x-spacing " items-center space-x-2 ")
(def small-sb " text-xs font-semibold ")
(def large-sb " text-2xl font-semibold ")

(def conversation-css
  {:conv/top           {:class (str flex-col "w-3/5 border-l border-r border-gray-400")}
   :conv/main          {:class "flex-auto overflow-y-auto p-5 space-y-4"
                        :style {:background-image conversation-background-img}}
   :conv/convinput     {:class "flex-none h-40 p-4 pt-0"}
   :conv/textarea      {:class "w-full h-full outline-none border focus:border-blue-600 hover:border-blue-600 rounded p-4 shadow-lg"}

   :hint/top           {:class (str flex-row "justify-center text-sm text-gray-600")}

   :part/top           {:class (str flex-row "space-x-2 ")}
   :part/topreverse    {:class (str flex-row "space-x-2 flex-row-reverse space-x-reverse")}
   :part/iconc         {:class "flex-none w-6 h-6"}
   :part/cont          {:class flex-col}
   :part/msgc          {:class "bg-gray-200 rounded p-5"}
   :part/timec         {:class "text-sm text-gray-600"}

   :action/iconc       {:class "w-4 h-4"}

   :header/top         {:class (str flex-row justify-center "flex-none h-20 p-5 border-b")}
   :header/person-top  {:class (str flex-col "space-y-1")}
   :header/input       {:class "text-sm outline-none border-b border-dashed text-black placeholder-gray-600"}
   :header/act-cont    {:class (str flex-row center-x-spacing)}
   :settings/top       {:class (str flex-col "w-1/5 bg-gray-200 overflow-y-auto")}
   :settings/part1     {:class (str flex-col "h-64 flex-none border-b border-gray-400")}
   :settings/container {:class (str flex-col "space-y-4 p-4")}})

(def sidebar-css
  {:main/top       {:class (str flex-col justify-center "flex-none w-16 bg-gray-200")}
   :main/container {:class (str flex-col "w-full pt-5")}
   
   :item/top       {:class (str  flex-row justify-center "block relative w-full p-4 h-16 w-16")}
   :item/topactive {:class (str  flex-row justify-center "block relative w-full p-4 h-16 w-16 bg-gray-100")}
   :item/iconc     {:class "flex-none w-7 h-7"}
   :item/nonicon   {:class "rounded-full bg-gray-400 w-8 h-8"}
   :item/countc    {:class "absolute top-0 right-0 mr-3 mt-3 bg-red-500 w-4 h-4 text-xs text-white rounded-full text-center"}})

(def stream-css
  {:you/top         {:class (str flex-col "w-1/5")}
   :you/blocks      {:class "flex-auto overflow-y-auto"}

   :menu/top        {:class (str flex-row justify-center "h-8 p-3 text-xs")}
   :menu/container  {:class (str flex-row justify-center "space-x-2")}

   :header/top      {:class (str flex-row center-x-spacing "p-3 ")}
   :header/svgc     {:class "flex-none w-5 h-5"}
   :header/h1       {:class (str large-sb "flex-grow")}

   :block/top       {:class "block border-b "}
   :block/currentc  {:class "border-blue-500 bg-blue-100 border-l-2 p-3 space-y-4"}
   :block/nocurrent {:class "border-transparent hover:bg-gray-100 border-l-2 p-3 space-y-4"}
   :block/container {:class (str flex-row center-x-spacing)}
   :block/svg-big   {:class "w-5 h-5"}
   :block/svg-small {:class "flex-none w-3 h-3"}
   :block/personc   {:class "flex-grow text-sm"}
   :block/timec     {:class "text-xs text-gray-600"}
   :block/msgc      {:class "flex-grow truncate text-xs"}})

(def inbox-css
  {:inbox/top             {:class (str flex-col "w-64 flex-none bg-gray-100 p-4 space-y-6")}
   :inbox/header          {:class (str flex-row justify-center "mb-6")}
   :inbox/h1c             {:class large-sb}
   :inbox/svgc            {:class "flex-none w-4 h-4"}

   :block/top             {:class (str " space-y-3 ")}
   :block/buttonc         {:class (str  justify-center "inline-flex focus:outline-none ")}
   :block/svgc            {:class "flex-none w-3 h-3 ml-2"}
   :block/container       {:class (str "space-y-3 pb-4")}
   :block/containerhidden {:class (str "space-y-3 pb-4 hidden")}

   :action/top            {:class (str flex-row center-x-spacing "ml-1")}
   :action/svgc           {:class "w-5 h-5"}
   :action/namec          {:class (str small-sb "flex-grow text-gray-600")}

   :view/top              {:class (str flex-row center-x-spacing " ml-1 text-xs")}
   :view/svgc             {:class "w-5 h-5"}
   :view/namec            {:class "flex-grow "}
   :view/countc           {:class "text-gray-600 "}

   :expand/top            {:class (str flex-row center-x-spacing small-sb "ml-1 text-gray-600")}
   :expand/msgc           {:class "flex-grow "}
   :expand/buttonc        {:class (str small-sb "text-gray-600")}})

(defn sidebar-item [{:keys [count icon active?]}]
  (let [{:item/keys [top topactive iconc nonicon countc]} sidebar-css
        top (if active? topactive top)]
   [:a  (merge top  {:href "#"})
    (if icon
      [svg iconc icon]
      [:div nonicon])
    (when count
      [:div countc count])]))

(defn sidebar [{:keys [sidebar-items1 sidebar-items2]}]
  (let [{:main/keys [top container]} sidebar-css]
   [:div#sidebar top
    [:div container
     (u/spread-by-order sidebar-item sidebar-items1)]

    [:div container
     (u/spread-by-order sidebar-item sidebar-items2)]]))

(defn inbox-view [{:keys [icon name count]}]
  (let [{:view/keys [top svgc namec countc]} inbox-css]
   [:div top
    [svg svgc icon]

    [:div namec name]
    [:div countc count]]))

(defn inbox-view-expand [{:keys [ msg action]}]
  (let [{:expand/keys [top msgc buttonc]} inbox-css]
   [:div top
    [:div msgc msg]
    [:button buttonc  action]]))

(defn inbox-action [{:keys [icon name]}]
  (let [{:action/keys [top svgc namec]} inbox-css]
   [:div top
    [svg svgc icon]
    [:div namec name]]))

(defn inbox-block [{:keys [title open?]} fragm]
  (r/with-let [{:block/keys [top buttonc svgc container containerhidden]} inbox-css
               [open*? set-open] (use-state open?)]
               
    (let [icon   (if @open*? v/chevron-down v/chevron-left)
          container (if  @open*?  container containerhidden)]
      [:div top
       [:button (merge buttonc 
                       {:on-click #(set-open (not @open*?))})
        title
        [svg svgc icon]]
       [:div container
        fragm]])))

        
(defn inbox [{:keys [conversation-views]}]
  (let [ {:inbox/keys [top header h1c svgc]} inbox-css]
   [:div#inbox top
    [:div header
     [:h1 h1c "Inbox"]
     [svg svgc v/search-icon]]
    [inbox-block {:title "Conversations" :open? true}
     [:<>
      (u/spread-by-id inbox-view conversation-views)
      [inbox-action {:icon v/plus :name "Create View"}]
      [inbox-view-expand {:msg "See 124 more" :action "Edit"}]]]
    [inbox-block {:title "Automation"}
     [:<>
      [inbox-action {:icon v/plus :name "Create Automation"}]
      [inbox-view-expand {:msg "See 42 more" :action "Edit"}]]]
    [inbox-block {:title "Your preferences"}]]))  

(defn stream-block [{:keys [person time msg current]}]
  (let [{:block/keys [top currentc nocurrent container svg-big svg-small personc timec msgc]} stream-css 
        blockcss (if current currentc nocurrent  )]
   [:a.conversation-block top
    [:div blockcss
     [:div container
      [svg svg-big v/user-circle]

      [:strong personc person]
      [:div timec time]]
     [:div container
      [svg svg-small v/user-circle]
      [:div msgc msg]]]]))

(defn stream-header [{:keys [icon name]}]
  (let [{:header/keys [top svgc h1]} stream-css]
   [:div top
    [svg svgc icon]
    [:h1 h1 name]]))

(defn stream-menu []
  (let [{:menu/keys [top container]} stream-css]
   [:div top
    [:div container
     [svg {} v/brief-case]
     [:div 5]
     [svg {} v/chevron-down]]
    [:div container
     [:div "newest"]
     [svg {} v/chevron-down]]]))

(defn you-stream [{:keys [cbd-list]}]
  (let [{:you/keys [top blocks]} stream-css]
   [:div#you-stream top
    [stream-header {:icon v/menu-alt-1 :name "You"}]
    [stream-menu]
    [:div blocks
     (u/spread-by-id stream-block cbd-list)]]))


(defn conversation-header-action [{:keys [icon]}]
  (let [{:action/keys [iconc]} conversation-css]
   [svg iconc icon]))

(defn conversation-header [{:keys [person title actions]}]
  (let [{:header/keys [top person-top input act-cont]} conversation-css
        title  (or title "")]
   [:div top
    [:div person-top
     [:strong person]
     [:input (merge input
              {:type "text", 
               :placeholder "Add coversation title" 
               :value title
               :on-change identity})]]
    [:div act-cont
     (u/spread-by-order conversation-header-action actions)]]))


(defn conversation-hint [{:keys [msg]}]
  (let [{:hint/keys [top]} conversation-css]
   [:div top  msg]))

(defn conversation-part [{:keys [icon msg time me]}]
  (let [{:part/keys [top topreverse iconc cont msgc timec ]} conversation-css
         top (if me  topreverse top)]
   [:div top
    [svg iconc icon]
    [:div cont
     [:div msgc msg]
     [:div timec time]]]))

(defn conversation-item [ data]
  (condp = (:type data)
    :hint [conversation-hint  data]
    [conversation-part data]))

(def conversation-header-actions
  [{:icon v/dots-vertical} 
   {:icon v/user-circle} 
   {:icon v/star} 
   {:icon v/clock} 
   {:icon v/check}])

(defn conversation [{:keys [ conversations]}]
  (let [{:conv/keys [top main convinput textarea]} conversation-css]
   [:div#conversation top
    [conversation-header {:person db/nt :actions conversation-header-actions}]
    [:div main
     (u/spread-by-order conversation-item conversations)]
    [:div convinput
     [:textarea
      (merge textarea
             {:value "Hi"
              :on-change identity})]]]))

(defn card []
  [:div {:class (str flex-row justify-center "flex-none h-64 bg-white border rounded p-4")} "card content"])

(defn conversation-settings []
  (let [{:settings/keys [top part1 container]} conversation-css]
   [:div#conversation-settings top
    [:div part1]
    [:div container
     [card]
     [card]
     [card]]]))

(def pageconfig
  {::sidebar              {:defaults db/data
                           :render   sidebar}
   ::inbox                {:defaults db/data
                           :render   inbox}
   ::you-stream           {:defaults db/data
                           :render you-stream}
   ::conversation         {:defaults  db/data
                           :render conversation}
   ::conversation-details {;;:defaults   db/data
                           :render conversation-settings}})
(def main-css
  {:main/top {:class (str flex-row "h-screen bg-gray-100")}
   :main/content  {:class (str flex-row "flex-auto bg-white rounded-tl-xl border-l shadow-xl")}})

(defn main-component [{:keys [::sidebar ::inbox ::you-stream ::conversation ::conversation-details]}]
  (let [{:main/keys [top content]} main-css]
   [:div top
    [sidebar]
    [inbox]
    [:div content
     [you-stream]
     [conversation]
     [conversation-details]]]))


(defn main []
  [main-component (ig/init pageconfig)])

