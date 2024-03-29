(ns mailclient.views.tw-classes)

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