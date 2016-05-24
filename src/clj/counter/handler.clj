(ns counter.handler
  (:require [compojure.core :refer [GET POST DELETE defroutes routes]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [compojure.route :refer [not-found resources]]
            [compojure.coercions :refer  [as-int]]
            [ring.middleware.json :refer  [wrap-json-body wrap-json-response]]
            [hiccup.page :refer [include-js include-css html5]]
            [counter.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(def page
  (html5
    (head)
    [:body {:class "body-container"}
     [:div#app]
     (include-js "/js/app.js")]))

(defn format-resp [cnt]
  {:body (reduce-kv (fn [acc k v] (conj acc (assoc v :id k))) [] cnt)})

(defonce db (atom {}))

(defn count-all []
  (format-resp @db))

(defn count-create [body]
  (let [last-id (if (empty? @db) 0 (apply max (keys @db)))
        item {:title (:title body) :count 0}]
    (do (swap! db #(assoc % (inc last-id) item))
        (format-resp @db))))

(defn count-adjust [body adjustment]
  (let [id (Integer. (:id body))
        item (@db id)
        adjusted-item (assoc item :count (adjustment (:count item)))]
    (do (swap! db #(assoc % id adjusted-item))
        (format-resp @db))))

(defn count-del [body]
  (let [id (Integer. (:id body))]
    (do (swap! db #(dissoc % id))
        (format-resp @db))))

(defroutes api-routes
  (GET "/api/v1/counters" [] (count-all))
  (POST "/api/v1/counters" {body :body} (count-create body))
  (POST "/api/v1/counters/inc" {body :body} (count-adjust body inc))
  (POST "/api/v1/counters/dec" {body :body} (count-adjust body dec))
  (DELETE "/api/v1/counters" {body :body} (count-del body)))

(defroutes site-routes
  (GET "/" [] page)
  (resources "/")
  (not-found "Not Found"))

(def app
  (routes
    (-> api-routes
        (wrap-json-response)
        (wrap-json-body {:keywords? true})
        (wrap-defaults api-defaults))
    (wrap-middleware #'site-routes)))
