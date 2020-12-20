(ns dinsro.router
  (:require
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.routing.dynamic-routing :as dr :refer [defrouter]]
   [dinsro.views.admin :as v.admin]
   [dinsro.views.home :as v.home]
   [dinsro.views.index-accounts :as v.index-accounts]
   [dinsro.views.index-categories :as v.index-categories]
   [dinsro.views.index-currencies :as v.index-currencies]
   [dinsro.views.index-rates :as v.index-rates]
   [dinsro.views.index-rate-sources :as v.index-rate-sources]
   [dinsro.views.index-transactions :as v.index-transactions]
   [dinsro.views.index-users :as v.index-users]
   [dinsro.views.login :as v.login]
   [dinsro.views.registration :as v.registration]
   [dinsro.views.settings :as v.settings]
   [dinsro.views.show-account :as v.show-account]
   [dinsro.views.show-currency :as v.show-currency]
   [dinsro.views.show-user :as v.show-user]
   [taoensso.timbre :as timbre]))

(comment v.settings/SettingsPage)

(defrouter RootRouter
  [_this {:keys [current-state]}]
  {:router-targets [v.admin/AdminPage
                    v.home/HomePage
                    v.index-accounts/IndexAccountsPage
                    v.index-categories/IndexCategoriesPage
                    v.index-currencies/IndexCurrenciesPage
                    v.index-rates/IndexRatesPage
                    v.index-rate-sources/IndexRateSourcesPage
                    v.index-transactions/IndexTransactionsPage
                    v.index-users/IndexUsersPage
                    v.login/LoginPage
                    v.registration/RegistrationPage
                    v.show-account/ShowAccountPage
                    v.show-currency/ShowCurrencyPage
                    v.show-user/ShowUserPage]}
  (case current-state
    :pending (dom/div "Loading...")
    :failed (dom/div "Failed!")
    ;; default will be used when the current state isn't yet set
    (dom/div "No route selected.")))

(def ui-root-router (comp/factory RootRouter))
