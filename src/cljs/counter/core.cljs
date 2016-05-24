(ns counter.core
  (:require [reagent.core :as r]
            [counter.components.app :refer [app]]))

(defn mount-root []
  (r/render [app] (.getElementById js/document "app")))

(defn init! [] (mount-root))
