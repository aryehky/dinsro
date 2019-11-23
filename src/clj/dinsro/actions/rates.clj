(ns dinsro.actions.rates
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [dinsro.model.rates :as m.rates]
            [dinsro.spec.rates :as s.rates]
            [orchestra.core :refer [defn-spec]]
            [ring.util.http-response :as http]
            [taoensso.timbre :as timbre]))

(s/def :create-rates-valid/params (s/keys :req-un [::s.rates/value]))
(comment
  (gen/generate (s/gen :create-rates-valid/params))
  )

(s/def :create-rates/params (s/keys :opt-un [::s.rates/name]))
(comment
  (gen/generate (s/gen :create-rates/params))
  )

(s/def :create-rates-response/items (s/coll-of ::s.rates/item))
(s/def :create-rates-response/body (s/keys :req-un [:create-rates-response/items]))

(s/def :read-rates-response/body (s/keys))
(comment
  (gen/generate (s/gen :read-rates-response/body))
  )

(s/def ::create-handler-request (s/keys :req-un [:create-handler/params]))
(comment
  (gen/generate (s/gen ::create-handler-request))
  )

(s/def ::create-handler-response (s/keys :req-un [:create-rates-response/body]))
(comment
  (gen/generate (s/gen ::create-handler-response))
  )

(s/def ::index-handler-request (s/keys))
(s/def ::index-handler-response (s/keys))

(s/def ::read-handler-request (s/keys))
(comment
  (gen/generate (s/gen ::read-handler-request))
  )

(s/def ::read-handler-response (s/keys :req-un [:read-rates-response/body]))
(comment
  (gen/generate (s/gen ::read-handler-response))
  )

(s/def :create-rates-valid/request (s/keys :req-un [:create-rates-valid/params]))
(comment
  (gen/generate (s/gen :create-rates-valid/request))
  )

(defn-spec index-handler ::index-handler-response
  [request ::index-handler-request]
  (let [items (m.rates/index-records)]
    (http/ok {:model :rates :items items})))

(defn-spec create-handler ::create-handler-response
  [request ::create-handler-request]
  (http/ok {:item {:db/id 1}}))

(defn-spec read-handler ::read-handler-response
  [request ::read-handler-request]
  (http/ok {:status "ok"}))
