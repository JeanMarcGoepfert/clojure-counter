(ns counter.components.counter-row
  (:require [counter.actions :refer [delete-item increase-item decrease-item]]))

(defn counter-row [data]
  [:li {:key (:id data)}
   (:count data) " - " (:title data)
   [:button {:on-click #(delete-item   (:id data))} "delete"]
   [:button {:on-click #(decrease-item (:id data))} "-"]
   [:button {:on-click #(increase-item (:id data))} "+"]])
