(ns counter.ajax
  (:require [cljs.core.async :refer [chan put!]]))

(def base-url "http://localhost:3000")

(defn ajax
  ([method url]
   (ajax method url {}))
  ([method url body]
   (let [ch (chan)]
     (method (str base-url url) (merge {:format          :json
                                        :response-format :json
                                        :keywords?       true
                                        :handler         #(put! ch %)}
                                       body))
     ch)))
