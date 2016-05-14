(ns counter.core
    (:require [reagent.core :as reagent :refer [atom]]))

(defn home-page []
  [:div [:h2 "Welcome to counter"]
   [:div [:a {:href "/about"} "go to about page"]]])

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! [] (mount-root))
