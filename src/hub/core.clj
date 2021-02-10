(ns hub.core
    (:require   [hub.web.server :as server]
                [hub.services.service :as svc])
  (:gen-class))

(defn -main
  "Bootstrap system as a web server and expose endpoints for submission of data."
  [& args]
  (server/run))
