(defproject liberator-friend "0.1.0-SNAPSHOT"
  :description "Example of Friend and Liberator integration."
  :url "http://github.com/sritchie/liberator-friend"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main liberator-friend.core
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.cemerick/friend "0.2.0"]
                 [liberator "0.10.0"]
                 [compojure "1.1.5"]
                 [http-kit "2.1.13"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [ring/ring-devel "1.2.0"]
                 [hiccup "1.0.1"]])
