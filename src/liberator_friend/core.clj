(ns liberator-friend.core
  (:gen-class)
  (:require [liberator-friend.misc :as misc]
            [liberator-friend.middleware.auth :as auth]
            (compojure handler [route :as route])
            [compojure.core :as compojure :refer (GET defroutes)]
            [hiccup.core :as h]
            [hiccup.element :as e]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as rl]
            [ring.middleware.resource :refer [wrap-resource]]))

(defn- demo-vars
  [ns]
  {:namespace ns
   :ns-name (ns-name ns)
   :name (-> ns meta :name)
   :doc (-> ns meta :doc)
   :route-prefix (misc/ns->context ns)
   :app (ns-resolve ns 'app)
   :page (ns-resolve ns 'page)})

(defroutes landing
  (GET "/admin" [] "Hi!")
  (GET "/user" [] "Available to the user only!")
  (GET "/" req (h/html [:html
                        misc/pretty-head
                        (misc/pretty-body
                         [:h1 {:style "margin-bottom:0px"}
                          [:a {:href "http://github.com/cemerick/friend-demo"} "Among Friends"]]
                         [:p {:style "margin-top:0px"} "…a collection of demonstration apps using "
                          (e/link-to "http://github.com/cemerick/friend" "Friend")
                          ", an authentication and authorization library for securing Clojure web services and applications."]
                         [:p "Implementing authentication and authorization for your web apps is generally a
necessary but not particularly pleasant task, even if you are using Clojure.
Friend makes it relatively easy and relatively painless, but I thought the
examples that the project's documentation demanded deserved a better forum than
to bit-rot in a markdown file or somesuch. So, what better than a bunch of live
demos of each authentication workflow that Friend supports (or is available via
another library that builds on top of Friend), with smatterings of
authorization examples here and there, all with links to the
generally-less-than-10-lines of code that makes it happen?"]
                         [:p "Check out the demos, find the one(s) that apply to your situation, and
click the button on the right to go straight to the source for that demo."])])))

(defn- wrap-app-metadata
  [h app-metadata]
  (fn [req] (h (assoc req :demo app-metadata))))

(def site
  (-> landing
      (auth/friend-middleware)))

(defonce server (atom nil))

(defn kill! []
  (swap! server (fn [s] (when s (s) nil))))

(defn -main
  "Main entry point. You can provide a string-based env map that will
   override the config specified in your env, if you like."
  []
  (swap! server
         (fn [s]
           (if s
             (do (println "Server already running!") s)
             (do (println "Booting server on port 8090.")
                 (run-server (rl/wrap-reload #'site) {}))))))

(defn running? []
  (identity @server))

(defn cycle! []
  (kill!)
  (-main))
