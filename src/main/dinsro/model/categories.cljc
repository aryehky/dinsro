(ns dinsro.model.categories
  (:refer-clojure :exclude [name])
  (:require
   [clojure.spec.alpha :as s]
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.guardrails.core :refer [>defn =>]]
   [dinsro.model.users :as m.users]
   [dinsro.specs]))

(s/def ::id uuid?)
(defattr id ::id :uuid
  {ao/identity? true
   ao/schema    :production})

(s/def ::name string?)
(defattr name ::name :string
  {ao/identities #{::id}
   ao/schema     :production})

(s/def ::user ::m.users/id)
(defattr user ::user :ref
  {ao/cardinality :one
   ao/required?   true
   ao/identities #{::id}
   ao/schema     :production
   ao/target     ::m.users/id
   ::report/column-EQL {::user [::m.users/id ::m.users/name]}})

(s/def ::params (s/keys :req [::name ::user]))
(s/def ::item (s/keys :req [::id ::name ::user]))
(s/def ::ident (s/tuple keyword? ::id))

(>defn ident
  [id]
  [::id => ::ident]
  [::id id])

(>defn ident-item
  [{::keys [id]}]
  [::item => ::ident]
  (ident id))

(defn idents
  [ids]
  (mapv (fn [id] {::id id}) ids))

(def attributes [id name user])

#?(:clj (def resolvers []))
