(ns hub.services.file.service
    (:require [clojure.core.async :as async]
              [hub.services.message.service :refer [publication]]
              [hub.utils.service-utils :refer [startup]]
              [hub.utils.file-utils :as futils])
    (:gen-class))

(def subscriber (async/chan))
(def subscription (async/sub publication :file subscriber))

(defn parse-file [message]
    "Reads a message and parses some data."
    (let [{:keys [content]} message]
        (futils/process)))

(defn init []
    (startup subscriber parse-file)
    (println "Started the file service."))
