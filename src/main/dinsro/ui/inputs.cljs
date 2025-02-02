(ns dinsro.ui.inputs
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [com.fulcrologic.fulcro.dom :as dom]
   [dinsro.model.accounts :as m.accounts]
   [dinsro.model.currencies :as m.currencies]
   [dinsro.model.users :as m.users]))

(defsc TextInput
  [_this {:keys [label value]} {:keys [onChange]}]
  {:query [:label :value]
   :initial-state
   (fn [{:keys [label value]
         :or   {label ""
                value ""}}]
     {:label label
      :value value})}
  (dom/div {}
    (dom/label :.label label)
    (dom/input :.input
      {:type     :text
       :value    value
       :onChange (fn [e] (onChange e))})))

(def ui-text-input
  (comp/computed-factory TextInput))

(defsc PasswordInput
  [_this {:keys [label value]} {:keys [onChange]}]
  {:query [:label :value]
   :initial-state
   (fn [{:keys [label value]
         :or   {label ""
                value ""}}]
     {:label label
      :value value})}
  (dom/div {}
    (dom/label :.label label)
    (dom/input :.input
      {:type     :password
       :value    value
       :onChange (fn [e] (onChange e))})))

(def ui-password-input
  (comp/computed-factory PasswordInput))

(defsc NumberInput
  [_this {:keys [label value]} {:keys [onChange]}]
  {:initial-state
   (fn [{:keys [label value]
         :or   {label ""
                value ""}}]
     {:label label
      :value value})
   :query [:label :value]}
  (dom/div {}
    (dom/label :.label label)
    (dom/input :.input
      {:type     :number
       :value    value
       :onChange onChange})))

(def ui-number-input
  (comp/computed-factory NumberInput))

(defsc SelectorOption
  [_this {:keys [id name]}]
  (dom/option {:value id} name))

(def ui-selector-option (comp/factory SelectorOption {:keyfn :id}))

(defsc AccountSelector
  [_this {:keys [accounts]}]
  {:initial-state {:accounts []}
   :query         [:accounts]}
  (dom/div :.select
    (dom/select {}
      (map (fn [{::m.accounts/keys [id name]}]
             (ui-selector-option {:id (str id) :name name}))
           accounts))))

(def ui-account-selector (comp/factory AccountSelector))

(defsc CurrencySelectorOption
  [_this {::m.currencies/keys [id name]}]
  {:ident         ::m.currencies/id
   :initial-state {::m.currencies/id   nil
                   ::m.currencies/name ""}
   :query         [::m.currencies/id ::m.currencies/name]}
  (dom/option {:value id} name))

(def ui-currency-selector-option (comp/factory CurrencySelectorOption {:keyfn ::m.currencies/id}))

(defsc CurrencySelector
  [_this {:keys [index]} {:keys [onChange]}]
  {:componentDidMount
   (fn [this] (df/load! this :index CurrencySelectorOption))
   :initial-state {:index []}
   :query         [{[:index '_] (comp/get-query CurrencySelectorOption)}]}
  (dom/div :.select
    (dom/select {:onChange onChange}
      (map ui-currency-selector-option index))))

(def ui-currency-selector (comp/computed-factory CurrencySelector))

(defsc UserSelector
  [_this {:keys [users]}]
  {:initial-state {:users []}
   :query         [:users]}
  (dom/div :.select
    (dom/select {}
      (map (fn [{::m.users/keys [id name]}]
             (ui-selector-option {:id (str id) :name name}))
           users))))

(def ui-user-selector (comp/factory UserSelector))

(defsc PrimaryButton
  [_this _props {:keys [classes onClick]}]
  {:initial-state {}
   :query         []}
  (dom/button :.ui.button.primary.submit.fluid
    {:classes classes
     :onClick onClick} "submit"))

(def ui-primary-button (comp/computed-factory PrimaryButton))
