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
  [["#" :routes/frontpage]
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

(defn tw [& classes]
  {:class (flatten (apply concat classes))})

(def vbox [:flex :flex-col])
(def hbox [:flex :flex-row])
(def hbox-ic [hbox :items-center])
(def bspread [:justify-between :items-center] )
(def hbox-spread [hbox bspread])
(def vbox-spread [vbox bspread])
(def size-3 [:w-3 :h-3])
(def size-4 [:w-4 :h-4])
(def size-5 [:w-5 :h-5])
(def size-6 [:w-6 :h-6])
(def size-7 [:w-7 :h-7])
(def size-8 [:w-8 :h-8])
(def size-16 [:w-16 :h-16])
(def xs-semibold [:text-xs :font-semibold])
(def _2xl-semibold [:text-2xl :font-semibold])
(def blue-white [:bg-blue-500 :text-white])


(def conversation-css
  {:action/iconc       size-4

   :card/top           [vbox [:h-64 :bg-white :border :border-gray-200 :rounded :p-4 :space-y-1]]
   :card/container     [hbox-ic [:space-x-2]]

   :conv/convinput     [:flex-none :h-40 :p-4 :pt-0]
   :conv/main          [:flex-auto :overflow-y-auto :p-5 :space-y-4]
   :conv/style         {:style {:background-image "url(https://static.intercomassets.com/ember/assets/images/messenger-backgrounds/background-1-99a36524645be823aabcd0e673cb47f8.png)"}}
   :conv/textarea      [:w-full :h-full :outline-none :border :focus:border-blue-600 :hover:border-blue-600 :rounded :p-4 :shadow-lg]
   :conv/top           [vbox ["w-3/5" :border-l :border-r :border-gray-400]]

   :header/act-cont    [hbox-ic [:space-x-2]]
   :header/input       [:text-sm :outline-none :border-b :border-dashed :text-black :placeholder-gray-600]
   :header/person-top  [vbox [:space-y-1]]
   :header/top         [hbox-spread [:flex-none :h-20 :p-5 :border-b]]

   :hint/top           [hbox [:justify-center :text-sm :text-gray-600]]

   :part/cont          vbox
   :part/iconc         [:flex-none size-6]
   :part/msgc          [:bg-gray-200 :rounded :p-5]
   :part/timec         [:text-sm :text-gray-600]
   :part/top           [hbox [:space-x-2]]
   :part/topreverse    [hbox [:space-x-2 :flex-row-reverse :space-x-reverse]]

   :settings/container [vbox [:space-y-4 :p-4]]
   :settings/cards-header hbox-spread
   :settings/part1     [vbox [:flex-none :border-b :border-gray-400 :p-4]]
   :settings/top       [vbox ["w-1/5" :bg-gray-200 :overflow-y-auto]]})

(def sidebar-css
  {:item/countc    [:rounded-full :text-center]
   :item/small-overlay  [size-4 :absolute :top-0 :right-0  :mr-3 :mt-3  :text-xs]
   :item/red-white [:bg-red-500 :text-white]
   :item/iconc     [:flex-none size-7]
   :item/nonicon   [:rounded-full :bg-gray-400 size-8]
   :item/top       [:block :relative :w-full :p-4 size-16]
   :item/topactive [:block :relative :w-full :p-4 size-16 :bg-gray-100]

   :main/container [vbox [:w-full :pt-5]]
   :main/top       [vbox-spread [:flex-none :w-16 :bg-gray-200]]})

(def stream-css
  {:block/container [hbox-ic [:space-x-2]]
   :block/currentc  [:border-blue-500 :bg-blue-100 :border-l-2 :p-3 :space-y-4]
   :block/msgc      [:flex-grow :truncate :text-xs]
   :block/nocurrent [:border-transparent :hover:bg-gray-100 :border-l-2 :p-3 :space-y-4]
   :block/personc   [:flex-grow :text-sm]
   :block/svg-big   size-5
   :block/svg-small [:flex-none size-3]
   :block/timec     [:text-xs :text-gray-600]
   :block/top       [:block :border-b]
   :header/h1       [_2xl-semibold :flex-grow]
   :header/svgc     [:flex-none size-5]
   :header/top      [hbox-ic [:space-x-2 :p-3]]
   :menu/container  [hbox [:space-x-2]]
   :menu/top        [hbox-spread [:h-8 :p-3 :text-xs]]
   :you/blocks      [:flex-auto :overflow-y-auto]
   :you/top         [vbox ["w-1/5"]]})

(def inbox-css
  {:expand/top            [hbox-ic [:space-x-2 xs-semibold :ml-1 :text-gray-600]]
   :action/namec          [xs-semibold :flex-grow :text-gray-600]
   :block/svgc            [:flex-none size-3 :ml-2]
   :block/buttonc         [bspread :inline-flex :focus:outline-none]
   :view/countc           [:text-gray-600]
   :view/namec            [:flex-grow]
   :inbox/header          [hbox-spread [:mb-6]]
   :action/svgc           size-5
   :action/top            [hbox-ic [:space-x-2 :ml-1]]
   :block/containerhidden [:space-y-3 :pb-4 :hidden]
   :expand/msgc           [:flex-grow]
   :inbox/h1c             [_2xl-semibold]
   :block/top             [:space-y-3]
   :block/container       [:space-y-3 :pb-4]
   :expand/buttonc        [xs-semibold :text-gray-600]
   :inbox/top             [vbox [:w-64 :flex-none :bg-gray-100 :p-4 :space-y-6]]
   :inbox/svgc            [:flex-none :w-4 :h-4]
   :view/svgc             size-5
   :view/top              [hbox-ic [:space-x-2 :ml-1 :text-xs]]})

(def component-css
  {:icon/textc [vbox :justify-center :rounded-full :text-center :flex-none]})

(defn text-icon [opts txt]
  (let [{:icon/keys [textc]} component-css]
   [:div (tw textc opts) txt]))

(defn sidebar-item [{:keys [count icon active?]}]
  (let [{:item/keys [top topactive iconc nonicon small-overlay red-white]} sidebar-css
        top (if active? topactive top)]
    [:a  (merge (tw top)  {:href "#"})
     (if icon
       [svg (tw iconc) icon]
       [:div (tw nonicon)])
     (when count
       (text-icon [small-overlay red-white] count))]))

(defn sidebar [{:keys [sidebar-items1 sidebar-items2]}]
  (let [{:main/keys [top container]} sidebar-css]
    [:div#sidebar (tw top)
     [:div (tw container)
      (u/spread-by-order sidebar-item sidebar-items1)]

     [:div (tw container)
      (u/spread-by-order sidebar-item sidebar-items2)]]))

(defn inbox-view [{:keys [icon name count]}]
  (let [{:view/keys [top svgc namec countc]} inbox-css]
    [:div (tw top)
     [svg (tw svgc) icon]

     [:div (tw namec) name]
     [:div (tw countc) count]]))

(defn inbox-view-expand [{:keys [msg action]}]
  (let [{:expand/keys [top msgc buttonc]} inbox-css]
    [:div (tw top)
     [:div (tw msgc) msg]
     [:button (tw buttonc)  action]]))

(defn inbox-action [{:keys [icon name]}]
  (let [{:action/keys [top svgc namec]} inbox-css]
    [:div (tw top)
     [svg (tw svgc) icon]
     [:div (tw namec) name]]))

(defn inbox-block [{:keys [title open?]} fragm]
  (r/with-let [{:block/keys [top buttonc svgc container containerhidden]} inbox-css
               [open*? set-open] (use-state open?)]

    (let [icon   (if @open*? v/chevron-down v/chevron-left)
          container (if  @open*?  container containerhidden)]
      [:div (tw top)
       [:button (merge (tw buttonc)
                       {:on-click #(set-open (not @open*?))})
        title
        [svg (tw svgc) icon]]
       [:div (tw container)
        fragm]])))


(defn inbox [{:keys [conversation-views]}]
  (let [{:inbox/keys [top header h1c svgc]} inbox-css]
    [:div#inbox (tw top)
     [:div (tw header)
      [:h1 (tw h1c) "Inbox"]
      [svg (tw svgc) v/search-icon]]
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

(defn stream-block [{:keys [person person-short  time msg current]}]
  (let [{:block/keys [top currentc nocurrent container svg-big svg-small personc timec msgc]} stream-css
        blockcss (if current currentc nocurrent)]
    [:a.conversation-block (tw top)
     [:div (tw blockcss)
      [:div (tw container)
       [text-icon [size-7 blue-white :font-semibold :text-sm] person-short]
       [:strong (tw personc) person]
       [:div (tw timec) time]]
      [:div (tw container)
       [svg (tw svg-small) v/user-circle]
       [:div (tw msgc) msg]]]]))

(defn stream-header [{:keys [icon name]}]
  (let [{:header/keys [top svgc h1]} stream-css]
    [:div (tw top)
     [svg (tw svgc) icon]
     [:h1 (tw h1) name]]))

(defn stream-menu []
  (let [{:menu/keys [top container]} stream-css]
    [:div (tw top)
     [:div (tw container)
      [svg {} v/brief-case]
      [:div 5]
      [svg {} v/chevron-down]]
     [:div (tw container)
      [:div "newest"]
      [svg {} v/chevron-down]]]))

(defn you-stream [{:keys [cbd-list]}]
  (let [{:you/keys [top blocks]} stream-css]
    [:div#you-stream (tw top)
     [stream-header {:icon v/menu-alt-1 :name "You"}]
     [stream-menu]
     [:div (tw blocks)
      (u/spread-by-id stream-block cbd-list)]]))


(defn conversation-header-action [{:keys [icon]}]
  (let [{:action/keys [iconc]} conversation-css]
    [svg (tw iconc) icon]))

(defn conversation-header [{:keys [person title actions]}]
  (let [{:header/keys [top person-top input act-cont]} conversation-css
        title  (or title "")]
    [:div (tw top)
     [:div (tw person-top)
      [:strong person]
      [:input (merge (tw input)
                     {:type "text"
                      :placeholder "Add coversation title"
                      :value title
                      :on-change identity})]]
     [:div (tw act-cont)
      (u/spread-by-order conversation-header-action actions)]]))


(defn conversation-hint [{:keys [msg]}]
  (let [{:hint/keys [top]} conversation-css]
    [:div (tw top)  msg]))

(defn conversation-part [{:keys [icon msg time me]}]
  (let [{:part/keys [top topreverse iconc cont msgc timec]} conversation-css
        top (if me  topreverse top)]
    [:div (tw top)
     [svg (tw iconc) icon]
     [:div (tw cont)
      [:div (tw msgc) msg]
      [:div (tw timec) time]]]))

(defn conversation-item [data]
  (condp = (:type data)
    :hint [conversation-hint  data]
    [conversation-part data]))

(def conversation-header-actions
  [{:icon v/dots-vertical}
   {:icon v/user-circle}
   {:icon v/star}
   {:icon v/clock}
   {:icon v/check}])

(defn conversation [{:keys [conversations]}]
  (let [{:conv/keys [top main style convinput textarea]} conversation-css]
    [:div#conversation (tw top)
     [conversation-header {:person db/nt :actions conversation-header-actions}]
     [:div (merge (tw main)
                  style)
      (u/spread-by-order conversation-item conversations)]
     [:div (tw convinput)
      [:textarea
       (merge (tw textarea)
              {:value "Hi"
               :on-change identity})]]]))

(defn card-item [{:keys [icon keyw]}]
  [:div  (tw hbox-ic [:text-sm :space-x-2])
   [svg  (tw [:h-4]) icon]
   [:p keyw]])

(def card-items
  [
   {:icon v/user :keyw "User"}
   {:icon v/globe :keyw "Owner"}
   {:icon v/at-symbol :keyw "Email"}
   {:icon v/phone :keyw "Phone"}
   {:icon v/plus :keyw "User id"}])
   

(defn card []
  (let [{:card/keys [top container]} conversation-css]
    [:div (tw top)
     [:div  (tw hbox-spread [:text-sm])
      [:div (tw container)
       [text-icon [ :h-7 :w-7 :font-semibold blue-white ] "NT"]
       [:div  (tw [:font-semibold])
        "Nikola Tesla"]]
      [:div (tw container)
       [svg {} v/dots-vertical]]]
     (u/spread-by-order card-item card-items)]))
     

(defn conv-details-item [[k v]]
  [:div
   (tw hbox [:space-x-2 :text-sm])
   [:p k] [:p v]])

(defn conv-details [details-items]
  [:div (tw [:space-y-1])
   [:div (tw hbox-spread)
    [:h3 (tw [:font-semibold])
     "Conversation details"]
    [svg (tw [:h-4 :w-4])
     v/cog]]


   (u/spread-by-order conv-details-item details-items)])

(defn conversation-settings [{:keys [details-items]}]
  (let [{:settings/keys [top part1 container]} conversation-css]
    [:div (tw top)
     [:div (tw part1)
      [conv-details details-items]]
     [:div (tw container)
      [:div (tw hbox-spread)
       [:h3 (tw [:font-semibold])
        "Related"]
       [:p "Customize"]]
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
   ::conversation-details {:defaults   db/data
                           :render conversation-settings}})
(def main-css
  {:main/top  [hbox [:h-screen :bg-gray-100]]
   :main/content  [hbox [:flex-auto :bg-white :rounded-tl-xl :border-l :shadow-xl]]})

(defn main-component [{:keys [::sidebar ::inbox ::you-stream ::conversation ::conversation-details]}]
  (let [{:main/keys [top content]} main-css]
    [:div (tw top)
     [sidebar]
     [inbox]
     [:div (tw content)
      [you-stream]
      [conversation]
      [conversation-details]]]))


(defn main []
  [main-component (ig/init pageconfig)])
