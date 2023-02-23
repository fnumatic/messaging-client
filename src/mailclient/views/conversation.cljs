(ns mailclient.views.conversation
  (:require [mailclient.svg :as v :refer [svg]]
            [mailclient.views.tw-classes :refer [bspread conversation-css
                                                 def-texticon hbox ic-x2]]
            [mailclient.views.utils :as u :refer [text-icon]]
            [tools.tailwindtools :refer [tw twl]]))





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

(defn conversation-editor [{:keys [person/id reply-msg update-msg send-msg note update-note save-note reply? change-type] }]
  (let [{:edit/keys [top header textarea active nonactive toolbar actions button]} (twl conversation-css)
        [reply notec] (if (= reply? :reply) [active nonactive] [nonactive active])
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
        {:conv/keys [top main]} (twl conversation-css)]
    [:div#conversation top
     (if (nil? id)
       [:div (merge main style)
        [:strong  "No Conversation selected"]]
       [:<>
        [header]
        [:div (merge main style)
         (u/spread-by-order conversation-item messages)]
        [editor]])]))


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
