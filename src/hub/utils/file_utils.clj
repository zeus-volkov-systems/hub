(ns hub.utils.file-utils
    (:require [clojure.java.io :as io]
              [clojure.string :refer [split split-lines replace]])
    (:gen-class))


(def input-files [(io/resource "inputs/1E3SalesRecords.csv")
            (io/resource "inputs/1E4SalesRecords.csv")])

(def output-file (io/resource "outputs/output.csv"))

(def get-five-european-records-transducer
    (comp
        (filter #(= "Europe" (:Region %)))
        (take 5)))


(defn make-inmem-maps [f]
    (let [inmem (split-lines (slurp f))
          headers (map keyword (split (replace (first inmem) #" " "_") #","))
          record-strings (rest inmem)]
          (into []
              (->> record-strings
              (map #(split % #","))
              (map #(zipmap headers %))))))

(defn mapvec-to-strings [v]
    (into [] (for [m v :let [{:keys [Region Country Sales_Channel]} m]] (str Region "," Country "," Sales_Channel))))

(defn write-to-file [records]
    (println records)
    (dorun (map #(spit output-file (str % "\n") :append true) records)))

(defn process []
    (let [[f1 f2] (for [f input-files] (make-inmem-maps f))
          all-records ((comp vec flatten conj) f1 f2)
          write-records (mapvec-to-strings (into [] get-five-european-records-transducer all-records))]
    (write-to-file write-records)))
