(ns hub.services.message.service
    (:require [clojure.core.async :as async]
              [clj-time.core :as time])
    (:gen-class))

;the central message publisher which other services should use to route all messages
(def publisher (async/chan))
;the multicast publisher for the central publisher, broadcasts on topic key
(def publication (async/pub publisher :topic))

(defn prepare [topic content]
    "Prepares a message by adding a unique id and a timestamp and converting a topic to a keyword"
    { :id (.toString (java.util.UUID/randomUUID))
      :timestamp (time/now)
      :topic (keyword topic)
      :content content})

(defn log-message [message]
    (let [{:keys [id topic timestamp content]} message]
        {:id id
         :topic :log
         :timestamp timestamp
         :sourcetopic topic
         :content content}))

(defn publish [topic content]
    "Prepares and publishes a message in the central publisher"
    (let [message (prepare topic content)]
        (async/put! publisher (log-message message))
        (async/put! publisher message)
        '"success"))

(defn init []
    (println "Started the message service."))
