(ns cljs-reframe-template.testdata
  (:require [cljs-reframe-template.svg :as v]
            [nano-id.core :refer [nano-id]]
            ))


(def firstn
  ["Aksel"
   "Brion"
   "Salomon"
   "Hetty"
   "Joann"
   "Albert"
   "Emmy"
   "Pembroke"
   "Warren"
   "Min"
   "Alvin"
   "Juana"
   "Cassie"
   "Donnie"
   "Melisse"
   "Annabell"
   "Betty"
   "Gage"
   "Mellisa"
   "Alfred"
   "Barris"
   "Krishna"
   "Sophi"
   "Jermain"
   "Town"
   "Thea"
   "Basile"
   "Reginauld"
   "Colene"
   "Alain"])

(def lastn
  ["Kristoffersson"
   "Dries"
   "Garza"
   "Potticary"
   "Danilchenko"
   "Thunnercliff"
   "De Bernardis"
   "Qusklay"
   "Losebie"
   "Rolland"
   "Pabst"
   "Murison"
   "Castilljo"
   "Creaser"
   "Reide"
   "Merryman"
   "Ryan"
   "Slyman"
   "Nevett"
   "Lowery"
   "Glidder"
   "Rainey"
   "Bursnell"
   "Frostdick"
   "Bellard"
   "Veasey"
   "Farden"
   "Volante"
   "MacKee"
   "Albany"])

(def times
  ["2020-02-28T15:51:47Z"
   "2020-09-07T01:42:10Z"
   "2020-09-30T17:30:55Z"
   "2020-09-30T01:40:10Z"
   "2020-05-10T03:04:24Z"
   "2020-06-16T18:08:41Z"
   "2020-02-26T09:44:49Z"
   "2020-04-08T14:25:22Z"
   "2020-05-06T18:10:29Z"
   "2020-07-15T23:30:07Z"
   "2020-06-29T17:58:45Z"
   "2021-01-24T03:53:46Z"
   "2020-10-19T15:57:07Z"
   "2020-10-29T19:00:52Z"
   "2020-03-17T07:54:04Z"
   "2020-04-27T15:22:49Z"
   "2020-05-25T02:30:53Z"
   "2020-11-12T04:16:36Z"
   "2020-11-12T08:42:36Z"
   "2020-07-19T20:26:36Z"
   "2020-03-30T23:56:13Z"
   "2020-12-26T13:24:38Z"
   "2021-01-10T07:43:30Z"
   "2020-03-23T06:40:30Z"
   "2021-02-21T03:57:07Z"
   "2020-08-30T21:30:06Z"
   "2020-10-16T14:57:30Z"
   "2020-03-27T03:54:45Z"
   "2020-06-23T13:29:44Z"
   "2020-12-22T08:00:03Z"
   "2020-04-10T05:28:24Z"
   "2021-01-16T13:23:35Z"
   "2021-01-20T08:33:15Z"
   "2020-08-16T03:09:17Z"
   "2020-05-02T08:12:10Z"
   "2020-06-19T11:49:00Z"
   "2020-04-26T23:48:56Z"
   "2020-03-01T17:01:18Z"
   "2020-07-21T00:05:19Z"
   "2020-08-15T21:19:56Z"
   "2020-04-09T13:39:40Z"
   "2020-09-23T15:06:14Z"
   "2020-09-08T17:13:44Z"
   "2020-11-12T17:01:53Z"
   "2020-08-04T04:29:20Z"
   "2020-07-08T22:23:01Z"
   "2020-04-18T02:57:48Z"
   "2020-10-25T02:36:54Z"
   "2020-05-07T12:42:47Z"
   "2021-02-11T10:08:36Z"])

(def icons
  #:icon{:chip v/chip
         :clock v/clock
         :phone v/phone
         :paper-airplane v/paper-airplane
         :chart-bar v/chart-bar
         :globe v/globe
         :users v/users
         :star v/star
         :plus v/plus
         :cog v/cog
         :template v/template
         :user v/user
         :menu-alt-1 v/menu-alt-1
         :brief-case v/brief-case
         :user-circle v/user-circle
         :search-icon v/search-icon
         :at-symbol v/at-symbol
         :chevron-left v/chevron-left
         :book-open v/book-open
         :bell v/bell
         :chevron-down v/chevron-down
         :dots-vertical v/dots-vertical
         :check v/check})

(def messages
  ["some message" "some info" "more of this" "more of that" "Very important!" "please note"
   "very cool" "Watch this!" "Hello, look"])


(defn create-conv [id]
  (let [[f l t] [(rand-nth firstn) (rand-nth lastn) (rand-nth times)]   ]
    {:id    (or id 0)
     :name  (str f " " l)
     :short (str (first f) (first l))
     :time  t  })
  )

(defn create-person
  ([] (create-person (nano-id)))
  ([id]
   (let [[f l t] [(rand-nth firstn) (rand-nth lastn) (rand-nth times)]]
     {:person/id    (or id 0)
      :name  (str f " " l)
      :short (str (first f) (first l))
      :reply-msg (str "Hello " (str f " " l))
      :note "Note to myself"
      :block-msg (rand-nth messages)
      :time  t})))