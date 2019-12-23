(ns dinsro.events.forms.add-user-account
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [dinsro.components :as c]
            [dinsro.components.debug :as c.debug]
            [dinsro.events.accounts :as e.accounts]
            [dinsro.events.users :as e.users]
            [dinsro.spec.accounts :as s.accounts]
            [dinsro.spec.events.forms.add-user-account :as s.e.f.add-user-account]
            [dinsro.spec.users :as s.users]
            [dinsro.specs :as ds]
            [dinsro.translations :refer [tr]]
            [kee-frame.core :as kf]
            [orchestra.core :refer [defn-spec]]
            [re-frame.core :as rf]
            [reframe-utils.core :as rfu]))

(rfu/reg-basic-sub ::s.e.f.add-user-account/shown?)
(rfu/reg-set-event ::s.e.f.add-user-account/shown?)

(defn toggle
  [cofx event]
  (let [{:keys [db]} cofx]
    {:db (update db ::s.e.f.add-user-account/shown? not)}))

(kf/reg-event-fx ::s.e.f.add-user-account/toggle toggle)

(def default-name "Offshore")

(rfu/reg-basic-sub ::s.e.f.add-user-account/name)
(rfu/reg-set-event ::s.e.f.add-user-account/name)

(rfu/reg-basic-sub ::s.e.f.add-user-account/initial-value)
(rfu/reg-set-event ::s.e.f.add-user-account/initial-value)

(rfu/reg-basic-sub ::s.e.f.add-user-account/currency-id)
(rfu/reg-set-event ::s.e.f.add-user-account/currency-id)

(rfu/reg-basic-sub ::s.e.f.add-user-account/user-id)
(rfu/reg-set-event ::s.e.f.add-user-account/user-id)

(defn-spec create-form-data (s/keys)
  [[name initial-value currency-id user-id] ::form-bindings _ any?]
  {:name          name
   :currency-id   (int currency-id)
   :user-id       (int user-id)
   :initial-value (.parseFloat js/Number initial-value)})

(comment
  (ds/gen-key ::s.e.f.add-user-account/form-bindings)
  (create-form-data ["Bob" "1" "1" "1"])
  (create-form-data (ds/gen-key ::s.e.f.add-user-account/form-bindings))
  )

(rf/reg-sub
 ::form-data
 :<- [::s.e.f.add-user-account/name]
 :<- [::s.e.f.add-user-account/initial-value]
 :<- [::s.e.f.add-user-account/currency-id]
 :<- [::s.e.f.add-user-account/user-id]
 create-form-data)
