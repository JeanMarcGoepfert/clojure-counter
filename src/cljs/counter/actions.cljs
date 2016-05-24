(ns counter.actions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [ajax.core :refer [GET POST DELETE]]
            [counter.ajax :refer [ajax]]
            [counter.store :refer [items]]))

(defn get-items []
  (go
    (let [response (<! (ajax GET "/api/v1/counters"))]
      (reset! items response))))

(defn add-item [title event]
  (go
    (let [new-item {:title title}
          response (<! (ajax POST "/api/v1/counters" {:params new-item}))]
      (reset! items response))))

(defn delete-item [id]
  (go
    (let [response (<! (ajax DELETE "/api/v1/counters" {:params {:id id}}))]
      (reset! items response))))

(defn increase-item [id]
  (go
    (let [response (<! (ajax POST "/api/v1/counters/inc" {:params {:id id}}))]
      (reset! items response))))

(defn decrease-item [id]
   (go
    (let [response (<! (ajax POST "/api/v1/counters/dec" {:params {:id id}}))]
      (reset! items response))))
