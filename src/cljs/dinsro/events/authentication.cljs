(ns dinsro.events.authentication
  (:require [ajax.core :as ajax]
            [clojure.spec.alpha :as s]

            [clojure.string :as string]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [dinsro.components :as c]
            [kee-frame.core :as kf]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [taoensso.timbre :as timbre]))

(c/reg-field ::auth-id nil)
(s/def ::auth-id (s/nilable string?))

(c/reg-field ::loading false)
(s/def ::loading boolean?)

(c/reg-field ::login-failed false)
(s/def ::login-failed boolean?)

;; Authenticate

(defn do-authenticate-success
  [db [{:keys [identity]}]]
  (-> db
      (assoc ::auth-id identity)
      (assoc ::loading false)
      (assoc ::login-failed false)))

(defn do-authenticate-failure
  [db _]
  (-> db
      (assoc ::login-failed true)
      (assoc ::loading false)))

(defn do-authenticate
  [{:keys [db]} [data]]
  {:db (assoc db ::loading true)
   :http-xhrio
   {:method          :post
    :uri             (kf/path-for [:api-authenticate])
    :params          data
    :timeout         8000
    :format          (ajax/json-request-format)
    :response-format (ajax/json-response-format {:keywords? true})
    :on-success      [::do-authenticate-success]
    :on-failure      [::do-authenticate-failure]}})

(kf/reg-event-db ::do-authenticate-success do-authenticate-success)
(kf/reg-event-db ::do-authenticate-failure do-authenticate-failure)
(kf/reg-event-fx ::do-authenticate do-authenticate)

;; Logout

(defn do-logout-success
  [db _]
  (assoc db ::auth-id nil))

;; You failed to logout. logout anyway
(defn do-logout-failure
  [db _]
  (assoc db ::auth-id nil))

(defn do-logout
  [_ _]
  {:http-xhrio
   {:uri             (kf/path-for [:api-logout])
    :method          :post
    :on-success      [::do-logout-success]
    :on-failure      [::do-logout-failure]
    :format          (ajax/json-request-format)
    :response-format (ajax/json-response-format {:keywords? true})}})

(kf/reg-event-db ::do-logout-success do-logout-success)
(kf/reg-event-db ::do-logout-failure do-logout-failure)
(kf/reg-event-fx ::do-logout do-logout)
