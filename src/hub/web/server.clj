(ns hub.web.server
    (:require [org.httpkit.server :refer [run-server]]
              [compojure.core :refer :all]
              [compojure.route :as route]
              [clojure.string :refer [split]]
              [hub.services.message.service :as msg]
              [hub.utils.map-utils :refer [keywordize-keys]])
    (:gen-class))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str "body")})

(defn run []
     (run-server app {:port 8080})
     (println "Server started on port 8080"))

(defn submit-job [request]
    (let [query-str (:query-string request)
          query-vec (split query-str #"&")
          query-map (keywordize-keys (into {} (map #(split % #"=") query-vec)))]
          (msg/publish (:topic query-map) (:content query-map))))

 (defroutes app
     (GET "/" [] submit-job)
     (route/not-found "<h1>Page not found</h1>"))

;http://localhost:8080/?topic=log&content=bar
