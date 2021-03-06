(ns cljs-reframe-template.views.home
  {:clj-kondo/config '{:lint-as {reagent.core/with-let clojure.core/let}}}
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r :refer [with-let]]
   [cljs-reframe-template.tools.reagent-hooks :refer [use-state]]
   [cljs-reframe-template.use-cases.core-cases :as ccases]
   [cljs-reframe-template.svg :as v :refer [svg]]
   [cljs-reframe-template.views.utils :as u]
   [integrant.core :as ig]
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

(defn tw 
  "nested coll of tw classes to :class map
   [:foo [:bar :buzz]] -> {:class [:foo :bar :buzz]}"
  [& classes]
  {:class (flatten (apply concat classes))})

(defn twon
 "tailwind on flag"
  [flag coll]
  (tw 
   (if flag 
     coll 
     (first coll))))

(defn twl 
  "all colls on mapvalues to :class maps
   {:foo [:bar]} -> {:foo {:class [:bar]}}"
  [tw-map]
  (reduce-kv
   #(assoc %1 %2 (tw %3))
   {}
   tw-map))

(def vbox [:flex :flex-col])
(def hbox [:flex :flex-row])
(def bspread [:justify-between :items-center])
(def size-3 [:w-3 :h-3])
(def size-4 [:w-4 :h-4])
(def size-5 [:w-5 :h-5])
(def size-6 [:w-6 :h-6])
(def size-7 [:w-7 :h-7])
(def size-8 [:w-8 :h-8])
(def size-16 [:w-16 :h-16])
(def xs-semibold [:text-xs :font-semibold])
(def sm-semibold [:text-sm :font-semibold])
(def _2xl-semibold [:text-2xl :font-semibold])
(def blue-white [:bg-blue-500 :text-white])
(def red-white [:bg-red-500 :text-white])
(def icon-sm [:flex-none size-3])
(def icon-lg [:flex-none size-6])
(def icon-xl [:flex-none size-8])
(def ic-x2 [:items-center :space-x-2])
(def def-texticon [size-7 blue-white sm-semibold])

(def main-css
  {:main/top  [hbox [:h-screen :bg-gray-100]]
   :main/content  [hbox [:flex-auto :bg-white :rounded-tl-xl :border-l :shadow-xl]]})

(def conversation-css
  {:action/iconc       size-4

   :card/top           [vbox :h-64 :bg-white :border :border-gray-200 :rounded :p-4 :space-y-1]
   :card/container     [hbox ic-x2]

   :conv/main          [:flex-auto :overflow-y-auto :p-5 :space-y-4]
   :conv/style         {:style {:background-image "url(https://static.intercomassets.com/ember/assets/images/messenger-backgrounds/background-1-99a36524645be823aabcd0e673cb47f8.png)"}}
   :conv/top           [vbox "w-3/5" :border-l :border-r :border-gray-400]
   :edit/textarea      [:w-full :h-full :outline-none :hover:border-blue-600  :resize-none]
   :edit/top           [vbox :flex-none :h-40 :p-4 :pt-2 :border :focus:border-blue-600 :shadow-lg :m-2 :rounded
                        :space-y-2 :hover:border-blue-600]
   :edit/header        [hbox  :space-x-6 :font-semibold :text-gray-500]
   :edit/active        [:text-blue-700 :border-b-2  :border-blue-700 :pb-1 :cursor-default]
   :edit/nonactive     [:cursor-pointer]
   :edit/menu-icon     [size-5 :text-gray-500]
   :edit/toolbar       [hbox bspread]
   :edit/actions       [hbox [:space-x-2]]
   :edit/button        [blue-white :font-medium :focus:outline-none :border-gray-400 :rounded :px-2 :py-0.5]

   :header/act-cont    [hbox ic-x2]
   :header/input       [:text-sm :outline-none :border-b :border-dashed :text-black :placeholder-gray-600]
   :header/person-top  [vbox :space-y-1]
   :header/top         [hbox bspread :flex-none :h-20 :p-5 :border-b]

   :hint/top           [hbox :justify-center :text-sm :text-gray-600]

   :part/cont          vbox
   :part/iconc         icon-lg
   :part/msgc          [:bg-gray-200 :rounded :p-5]
   :part/timec         [:text-sm :text-gray-600]
   :part/top           [hbox :space-x-2]
   :part/topreverse    [:flex :space-x-2 :flex-row-reverse :space-x-reverse]

   :settings/container [vbox :space-y-4 :p-4]
   :settings/cards-header [hbox bspread]
   :settings/part1     [vbox :flex-none :border-b :border-gray-400 :p-4]
   :settings/top       [vbox "w-1/5" :bg-gray-200 :overflow-y-auto]})

(def sidebar-css
  {:item/countc    [:rounded-full :text-center]
   :item/small-overlay  [size-4 :absolute :top-0 :right-0  :mr-3 :mt-3  :text-xs]
   :item/red-white red-white
   :item/iconc     icon-xl
   :item/nonicon   [:rounded-full :bg-gray-400 size-8]
   :item/top       [:block :relative :w-full :p-4 size-16]
   :main/container [vbox :w-full :pt-5]
   :main/top       [vbox bspread :flex-none :w-16 :bg-gray-200]})

(def stream-css
  {:block/container [hbox ic-x2]
   :block/block     [:border-l-2 :p-3 :space-y-4]
   :block/crnt      [:border-blue-500 :bg-blue-100]
   :block/nocrnt    [:border-transparent :hover:bg-gray-100]
   :block/msgc      [:flex-grow :truncate :text-xs]
   :block/personc   [:flex-grow :truncate :text-sm]
   :block/svg-big   size-5
   :block/svg-small icon-sm
   :block/timec     [:text-xs :text-gray-600]
   :block/top       [:block :border-b]
   :header/h1       [:2xl-semibold :flex-grow]
   :header/svgc     icon-lg
   :header/top      [hbox ic-x2 :p-3]
   :menu/container  [hbox ic-x2]
   :menu/top        [hbox bspread  :h-8 :p-3 :text-xs]
   :you/blocks      [:flex-auto :overflow-y-auto]
   :you/top         [vbox  "w-1/5"]})

(def inbox-css
  {:action/namec          [xs-semibold :flex-grow :text-gray-600]
   :action/svgc           size-5
   :action/top            [hbox ic-x2 :ml-1]
   :block/top             [:space-y-3]
   :block/svgc            [icon-sm :ml-2]
   :block/buttonc         [bspread :inline-flex :focus:outline-none]
   :block/container       [:space-y-3 :pb-4]
   :expand/top            [hbox ic-x2 xs-semibold :ml-1 :text-gray-600]
   :expand/msgc           [:flex-grow]
   :expand/buttonc        [xs-semibold :text-gray-600]
   :inbox/header          [hbox bspread  :mb-6]
   :inbox/h1c             [_2xl-semibold]
   :inbox/top             [vbox :w-64 :flex-none :bg-gray-100 :p-4 :space-y-6]
   :inbox/svgc            icon-sm
   :view/countc           [:text-gray-600]
   :view/namec            [:flex-grow]
   :view/svgc             size-5
   :view/top              [hbox ic-x2 :ml-1 :text-xs :font-semibold :cursor-pointer]})

(def component-css
  {:icon/textc [vbox :justify-center :rounded-full :text-center :flex-none]})

(defn text-icon [opts txt]
  (let [{:icon/keys [textc]} component-css]
   [:div (tw textc opts) txt]))

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

(defn inbox-view [{:keys [inbox/id icon name count current change-current] :as obj}]
  (let [{:view/keys [top ]} inbox-css
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
  (r/with-let [{:block/keys [container ]} inbox-css
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
       (u/spread-by-id2 inbox-view items :inbox/id opts)
       [inbox-action {:icon v/plus :name "Create View"}]
       [inbox-view-expand {:msg "See 124 more" :action "Edit"}]]]
     [inbox-block {:title "Automation"}
      [:<>
       [inbox-action {:icon v/plus :name "Create Automation"}]
       [inbox-view-expand {:msg "See 42 more" :action "Edit"}]]]
     [inbox-block {:title "Your preferences"}]]))

(defn stream-block [{:keys [person/id name short  time block-msg change-current current]}]
  (let [{:block/keys [crnt nocrnt block]} stream-css
        {:block/keys [top container svg-small personc timec msgc]} (twl stream-css)
        blockcss (if current [block crnt] [block nocrnt])]
    [:a.conversation-block  
     (merge top
            {:on-click #(change-current id)})
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

(defn stream-menu []
  (let [{:menu/keys [top container]} (twl stream-css)]
    [:div top
     [:div container
      [svg {} v/brief-case]
      [:div 5]
      [svg {} v/chevron-down]]
     [:div container
      [:div "newest"]
      [svg {} v/chevron-down]]]))

(defn you-stream [{:keys [items stream-name ] :as opts}]
  (let [{:you/keys [top blocks]} (twl stream-css)]
    [:div#you-stream top
     [stream-header {:icon v/menu-alt-1 :name stream-name}]
     [stream-menu]
     [:div blocks
      (u/spread-by-id2 stream-block items :person/id (select-keys opts [:change-current]))]]))


(defn conversation-header-action [{:keys [icon]}]
  (let [{:action/keys [iconc]} conversation-css]
    [svg (tw iconc) icon]))

(defn header-actions [user]
  [{:icon v/dots-vertical}
   user
   {:icon v/star}
   {:icon v/clock}
   {:icon v/check}])

(defn conversation-header [{:keys [person title action change-title]}]
  (let [{:header/keys [top person-top input act-cont]} (twl conversation-css)
        title  (or title "")
        actions (header-actions action)]
    [:div top
     [:div person-top
      [:strong person]
      [:input (merge input
                     {:type        "text"
                      :placeholder "Add conversation title"
                      :value       title
                      :on-change   (comp change-title u/target-value)})]]
     [:div act-cont
      (u/spread-by-order conversation-header-action actions)]]))


(defn conversation-hint [{:keys [msg]}]
  (let [{:hint/keys [top]} (twl conversation-css)]
    [:div top  msg]))

(defn conversation-part [{:keys [icon msg time me]}]
  (let [{:part/keys [top topreverse iconc cont msgc timec]} (twl conversation-css)
        top (if me  topreverse top)]
    [:div top
     (if me
       [svg iconc (:value icon)]
       [text-icon def-texticon icon])
     [:div cont
      [:div msgc msg]
      [:div timec time]]]))

(defn conversation-item [data]
  (condp = (:type data)
    :hint [conversation-hint  data]
    [conversation-part data]))

(def edit-menu-icons
  [v/book-open v/brief-case v/chart-bar v/chip
   v/clock v/globe v/paper-airplane v/phone v/plus v/star])

(defn icon-cmp [data]
  (let [{:edit/keys [menu-icon]} (twl conversation-css)]
   [svg menu-icon data]))

(defn conversation-editor [{:keys [person/id reply-msg update-msg send-msg note update-note save-note reply? change-type] :as obj}]
  (let [{:edit/keys [top header textarea active nonactive toolbar actions button]} (twl conversation-css)
        [reply notec ] (if (= reply? :reply) [active nonactive] [nonactive active])
        [update value action actionname] (if (= reply? :reply) 
                                           [update-msg reply-msg send-msg "Send"]
                                           [update-note note save-note "Save"])]
     [:div top
      [:div header
       [:a (merge reply {:on-click #(change-type :reply)}) "Reply"]
       [:a (merge notec {:on-click #(change-type :note)}) "Note"]]
      [:textarea
       (merge textarea
              {:value     value
               :on-change (comp  (partial update id) u/target-value)})]
      [:div toolbar
       [:div actions
        (u/spread-by-order icon-cmp edit-menu-icons)]
       [:button (merge button {:on-click action}) actionname]]]))
       
       
         
(defn conversation [{:keys [person/id messages editor header]}]
  (let [{:conv/keys [style]} conversation-css
        {:conv/keys [top main ]} (twl conversation-css)]
    [:div#conversation top
     (if (nil? id)
       [:div (merge main style)
        [:strong  "No Conversation selected"]]
       [:<>
        [header]
        [:div (merge main style)
         (u/spread-by-order conversation-item messages)]
        [editor]])]))
        
          
        
     

(defn card-item [{:keys [icon keyw]}]
  [:div  (tw hbox ic-x2 [:text-sm])
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
  (let [{:card/keys [top container]} (twl conversation-css)]
    [:div top
     [:div  (tw hbox bspread [:text-sm])
      [:div container
       [text-icon def-texticon "NT"]
       [:div  (tw [:font-semibold])
        "Nikola Tesla"]]
      [:div container
       [svg {} v/dots-vertical]]]
     (u/spread-by-order card-item card-items)]))
     

(defn conv-details-item [[k v]]
  [:div
   (tw hbox [:space-x-2 :text-sm])
   [:p k] [:p v]])

(defn conv-details [details-items]
  [:div (tw [:space-y-1])
   [:div (tw hbox bspread)
    [:h3 (tw [:font-semibold])
     "Conversation details"]
    [svg (tw [:h-4 :w-4])
     v/cog]]


   (u/spread-by-order conv-details-item details-items)])

(defn conversation-settings [{:keys [items]}]
  (let [{:settings/keys [top part1 container]} (twl conversation-css)]
    [:div top
     [:div part1
      [conv-details items]]
     [:div container
      [:div (tw hbox bspread)
       [:h3 (tw [:font-semibold])
        "Related"]
       [:p "Customize"]]
      [card]
      [card]
      [card]]]))

(def pageconfig
  {::sidebar              {:defaults {:activate-view #(rf/dispatch [:sidebar/set-active  %])}
                           :state (rf/subscribe [:sidebar/main]) 
                           :render   sidebar}
   ::inbox                {:defaults {:change-current #(rf/dispatch [:inbox/set-active %])}
                           :state    (rf/subscribe [:inbox/main])
                           :render   inbox}
   ::you-stream           {:defaults {:change-current #(rf/dispatch [:stream/set-current %])}
                           :state    (rf/subscribe [:stream/main])
                           :render   you-stream}
   ::conversation-editor  {:defaults {:update-msg #(rf/dispatch [:conversation/update-msg %1 %2])
                                      :send-msg  #(rf/dispatch [:conversation/send-msg-flow])
                                      :update-note (fn [id txt](rf/dispatch [:conversation/update-note id txt]))
                                      :save-note #(println "note saved")
                                      :change-type #(rf/dispatch [:conversation/change-type %])}
                           :state    (rf/subscribe [:conversation/editor])
                           :render   conversation-editor}
   ::conversation-header  {:defaults {:change-title #(rf/dispatch [:conversation/change-title %])}
                           :state    (rf/subscribe [:conversation/header])
                           :render    conversation-header}
   ::conversation         {:defaults {:editor  (ig/ref ::conversation-editor)
                                      :header  (ig/ref ::conversation-header)}
                           :state (rf/subscribe [:conversation/main])
                           :render conversation}
   ::conversation-details {:state (rf/subscribe [:conversation-detail/main])
                           :render conversation-settings}})


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
