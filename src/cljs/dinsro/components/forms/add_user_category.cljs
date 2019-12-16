(ns dinsro.components.forms.add-user-category
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [dinsro.components :as c]
            [dinsro.components.debug :as c.debug]
            [dinsro.events.accounts :as e.accounts]
            [dinsro.events.categories :as e.categories]
            [dinsro.events.users :as e.users]
            [dinsro.spec.accounts :as s.accounts]
            [dinsro.spec.users :as s.users]
            [dinsro.translations :refer [tr]]
            [kee-frame.core :as kf]
            [orchestra.core :refer [defn-spec]]
            [re-frame.core :as rf]
            [reframe-utils.core :as rfu]))

(s/def ::shown? boolean?)
(rfu/reg-basic-sub ::shown?)
(rfu/reg-set-event ::shown?)

(s/def ::name string?)
(rfu/reg-basic-sub ::name)
(rfu/reg-set-event ::name)

(s/def ::user-id string?)
(rfu/reg-basic-sub ::user-id)
(rfu/reg-set-event ::user-id)

(defn create-form-data
  [[name user-id] _]
  {:name          name
   :user-id       (int user-id)})

(rf/reg-sub
 ::form-data
 :<- [::name]
 :<- [::user-id]
 create-form-data)

(defn form
  []
  (let [form-data @(rf/subscribe [::form-data])]
    (when @(rf/subscribe [::shown?])
      [:div
       [c/close-button ::set-shown?]
       [c.debug/debug-box form-data]
       [c/text-input (tr [:name]) ::name ::set-name]
       [c/user-selector (tr [:user]) ::user-id ::set-user-id]
       [c/primary-button (tr [:submit]) [::e.categories/do-submit form-data]]])))
