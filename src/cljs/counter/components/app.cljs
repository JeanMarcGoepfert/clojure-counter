(ns counter.components.app
  (:require [counter.actions :refer [get-items]]
            [reagent.core :as r]
            [counter.components.counter-list :refer [counter-list]]
            [counter.components.form :refer [form]]
            [counter.components.welcome-message :refer [welcome-message]]))

(defn app []
  (r/create-class
    {:component-will-mount #(get-items)
     :reagent-render
     (fn []
       [:div
        [welcome-message]
        [form]
        [counter-list]])}))
