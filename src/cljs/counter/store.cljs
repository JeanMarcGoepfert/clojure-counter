(ns counter.store
  (:require [reagent.core :as r :refer [atom]]))

(defonce items (r/atom []))
