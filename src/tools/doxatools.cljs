(ns tools.doxatools 
  (:require [ribelo.doxa :as dx]))


(defn query-pull [db_ p q] 
  (->> db_
       (dx/q q)
       (dx/pull db_ p)))