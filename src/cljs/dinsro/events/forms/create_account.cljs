(ns dinsro.events.forms.create-account
  (:require [dinsro.events.accounts :as e.accounts]
            [kee-frame.core :as kf]
            [re-frame.core :as rf]
            [reframe-utils.core :as rfu]
            [taoensso.timbre :as timbre]))

(rfu/reg-basic-sub ::name)
(rfu/reg-set-event ::name)

(rfu/reg-basic-sub ::currency-id)
(rfu/reg-set-event ::currency-id)

(rfu/reg-basic-sub ::user-id)
(rfu/reg-set-event ::user-id)

(rfu/reg-basic-sub ::shown?)
(rfu/reg-set-event ::shown?)

(rfu/reg-basic-sub ::initial-value)
(rfu/reg-set-event ::initial-value)

(defn form-data-sub
  [[name initial-value currency-id user-id] _]
  {:name          name
   :currency-id   (int currency-id)
   :user-id       (int user-id)
   :initial-value (.parseFloat js/Number initial-value)})

(rf/reg-sub
 ::form-data-sub
 :<- [::name]
 :<- [::initial-value]
 :<- [::currency-id]
 :<- [::user-id]
 form-data-sub)

(defn submit-clicked
  [_ _]
  (let [form-data @(rf/subscribe [::account-data])]
    {:dispatch [::e.accounts/do-submit form-data]}))

(kf/reg-event-fx ::submit-clicked submit-clicked)
