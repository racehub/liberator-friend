(ns liberator-friend.middleware.auth
  (:require [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])))

;; ## Friend Middleware

(defn friend-middleware
  "Returns a middleware that enables authentication via Friend."
  [handler users]
  (let [friend-m {:credential-fn (partial creds/bcrypt-credential-fn users)
                  :workflows
                  ;; Note that ordering matters here. Basic first.
                  [(workflows/http-basic :realm "/")
                   ;; The tutorial doesn't use this one, but you
                   ;; probably will.
                   (workflows/interactive-form)]}]
    (-> handler
        (friend/authenticate friend-m))))
