(ns hub.utils.service-utils
    (:require [clojure.core.async :as async])
    (:gen-class))


(defn startup [subscriber action]
    "uses a go(loop) to look for messages in the provided subscription.
    Parks when no messages are coming through (not really running all the time)."
    (async/go-loop []
        (let [message (async/<! subscriber)]
            (action message)
            (recur))))
