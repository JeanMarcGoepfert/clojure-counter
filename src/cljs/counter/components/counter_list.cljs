(ns counter.components.counter-list
  (:require [counter.components.counter-row :refer [counter-row]]
            [counter.store :refer [items]]))

(defn counter-list []
  [:ul
   (map counter-row @items)])
