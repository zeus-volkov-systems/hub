(ns hub.services.log.service
    (:require [clojure.core.async :as async]
              [clojure.java.io :as io]
              [hub.services.message.service :refer [publication]]
              [hub.utils.service-utils :refer [startup]])
    (:gen-class))


(def subscriber (async/chan))
(def subscription (async/sub publication :log subscriber))
(def logfile (io/resource "logging/messages.txt"))

(defn log [message]
    "Adds a message to an output log file (specified by logfile def above)"
    (let [{:keys [id topic timestamp sourcetopic content]} message]
        (spit logfile (apply str id "," timestamp "," sourcetopic "," content "\n") :append true)))

(defn add-headers []
    "Adds a header row to an empty logfile for later consumption"
    (spit logfile "id,timestamp,topic,content\n"))

(defn init-logfile []
    "Initializes a logfile with headers if it's already empty"
    (let [contents (slurp logfile)]
        (if (= contents "") (add-headers))))

(defn init []
    (init-logfile)
    (startup subscriber log)
    (println "Started the log service."))
