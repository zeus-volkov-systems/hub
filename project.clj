(defproject hub "0.1.0-SNAPSHOT"
  :description "hub - a federated kappa-style WMS"
  :url "http://www.clojure.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                  [org.clojure/core.async "0.4.490"]
                  [clj-time "0.15.0"]
                  [http-kit "2.3.0"]
                  [compojure "1.6.1"]]
  :resource-paths ["resources"]
  :main ^:skip-aot hub.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
