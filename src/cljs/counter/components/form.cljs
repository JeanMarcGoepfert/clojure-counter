(ns counter.components.form
  (:require [reagent.core :as r]
            [counter.actions :refer [add-item]]))

(defn- handle-submit [value event]
  (do (.preventDefault event)
      (add-item @value event)
      (reset! value "")))

(defn- handle-change [value event]
  (reset! value (.-target.value event)))

(defn form []
  (let [value (r/atom "")]
    (fn []
      [:form {:on-submit #(handle-submit value %)}
       [:input {:value @value
                :on-change #(handle-change value %)}]])))
