(ns hub.services.service
    (:require [hub.services.message.service :as msg]
              [hub.services.log.service :as log]
              [hub.services.file.service :as file])
    (:gen-class))

(msg/init)
(log/init)
(file/init)
