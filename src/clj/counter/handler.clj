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

(def db (atom []))

(defn count-all []
  {:body @db})

(defn count-create [body]
  (let [last-id (get (last @db) :id 1)
        item {:id (inc last-id) :title (:title body) :count 0}]
    (do
      (swap! db #(conj % item))
      {:body @db})))

(defn count-create [body]
  (let [last-id (get (last @db) :id 0)
        item {:id (inc last-id) :title (:title body) :count 0}]
    (do
      (swap! db #(conj % item))
      {:body @db})))

(defn count-inc [body]
  (let [id (Integer. (:id body))]
    (swap! db #(map (fn [x]
                      (if (= (:id x) id)
                        (assoc x :count (inc (:count  x)))
                        x
                        )) %))))

(defn count-dec [body]
  (let [id (Integer. (:id body))]
    (swap! db #(map (fn [x]
                      (if (= (:id x) id)
                        (assoc x :count (dec (:count  x)))
                        x
                        )) %))))

(defn count-del [body]
  (let [id (Integer. (:id body))]
    (swap! db #(filter (fn [x]
                         (if-not (= id (:id x))
                           x)
                         ) %))))

(defroutes api-routes
  (GET "/api/v1/counters" [] (count-all))
  (POST "/api/v1/counters" {body :body} (count-create body))
  (POST "/api/v1/counters/inc" {body :body} (count-inc body))
  (POST "/api/v1/counters/dec" {body :body} (count-dec body))
  (DELETE "/api/v1/counters" {body :body} (count-del body)))

 (GET "/users/:id"  [id :<< as-int]  (count-inc id))

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
