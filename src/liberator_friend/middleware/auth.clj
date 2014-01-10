(ns liberator-friend.middleware.auth
  (:require [clout.core :as c]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [util :as util]
                             [credentials :as creds])
            [ring.util.request :as req]
            [ring.util.response :as response]))

;; ## Friend Middleware
;;
;; This defines a hierarchy of users. This matters more once we start
;; doing authentication on different roles - coach, regatta-admin,
;; etc.

; a dummy in-memory user "database"
(def users
  {"root" {:username "root"
           :password (creds/hash-bcrypt "admin_password")
           :roles #{::admin}}
   "jane" {:username "jane"
           :password (creds/hash-bcrypt "user_password")
           :roles #{::user}}})

(def default-login-uri
  "/login")

(defn friend-middleware
  "Returns a middleware that enables authentication via Friend."
  [handler]
  (let [friend-m {:credential-fn (partial creds/bcrypt-credential-fn users)
                  :workflows
                  ;; Note that ordering matters here. Basic first.
                  [(workflows/http-basic :realm "/")
                   (workflows/interactive-form)]}]
    (-> handler
        (friend/authenticate friend-m))))
