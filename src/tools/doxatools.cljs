(ns tools.doxatools 
  (:require [ribelo.doxa :as dx]))


(defn query-pull [db_ p q] 
  (->> db_
       (dx/q q)
       (dx/pull db_ p)))

(defn query-pull-s [db_ p q]
  (->> db_
       (dx/q q)
       (sort-by second)
       (dx/pull db_ p)))

(defn q-entity [key]
  `[:find [?e ...] :where [?e ~key]])