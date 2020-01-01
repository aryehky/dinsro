(ns dinsro.translations
  (:require [taoensso.tempura :as tempura]
            [taoensso.timbre :as timbre]))

(def dictionary
  {
   :missing {:missing "Missing"}

   :en
   {:missing "Missing text"
    :about "About"
    :actions "Actions"
    :account "Account"
    :accounts "Accounts"
    :admin "Admin"
    :categories "Categories"
    :currencies "Currencies"
    :currency "Currency"
    :currency-label "Currency: %1"
    :date "Date"
    :delete "Delete"
    :email "Email"
    :email-label "Email: %1"
    :fetch-accounts "Fetch Accounts: %1"
    :fetch-categories "Fetch Categories: %1"
    :fetch-currencies "Fetch Currencies: %1"
    :fetch-currency "Fetch Currency: %1 -> %2"
    :fetch-rate-sources "Fetch Rate Sources: %1"
    :fetch-rates "Fetch Rates: %1"
    :fetch-transactions "Fetch Transactions: %1"
    :fetch-users "Fetch Users: %1"
    :home-page "Home Page"
    :id-label "Id: %1"
    :index-accounts "Index Accounts"
    :initial-value "Initial Value"
    :initial-value-label "Initial Value: %1"
    :login "Login"
    :logout "Logout"
    :name "Name"
    :name-label "Name: %1"
    :no-accounts "No Accounts"
    :no-currencies "No Currencies"
    :no-rate-sources "No Rate Sources"
    :no-rates "No Rates"
    :no-users "No Users"
    :not-loaded "Not Loaded"
    :password "Password"
    :rate "Rate"
    :rate-sources "Rate Sources"
    :rate-sources-label "Rate Sources: %1"
    :rates "Rates"
    :register "Register"
    :settings "Settings"
    :submit "Submit"
    :time "Time"
    :toggle "Toggle"
    :transactions "Transactions"
    :user "User"
    :users "Users"
    :user-label "User: %1"
    :value "Value"}})

(def opts {:dict dictionary})

(def tr (partial tempura/tr opts [:missing]))

;; (defn tr
;;   ([k]
;;    (tr k []))
;;   ([k v]
;;    (tempura/tr opts [:missing] (timbre/spy :info k) (timbre/spy :info v))))
