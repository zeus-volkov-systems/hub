(ns hub.utils.map-utils
    (:gen-class))


(defn keywordize-keys [m]
    "converts a map with string keys to keyword keys and returns a new map"
    (into {} (for [[k v] m] [(keyword k) v])))
