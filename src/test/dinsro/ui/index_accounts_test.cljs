(ns dinsro.ui.index-accounts-test
  (:require
   [dinsro.model.accounts :as m.accounts]
   [dinsro.model.currencies :as m.currencies]
   [dinsro.model.users :as m.users]
   [dinsro.translations :refer [tr]]
   [dinsro.sample :as sample]
   [dinsro.ui.index-accounts :as u.index-accounts]
   [dinsro.ui.user-accounts :as u.user-accounts]
   [nubank.workspaces.card-types.fulcro3 :as ct.fulcro3]
   [nubank.workspaces.core :as ws]
   [nubank.workspaces.model :as wsm]
   [taoensso.timbre :as log]))

(ws/defcard IndexAccounts
  {::wsm/card-height 6
   ::wsm/card-width  4}
  (ct.fulcro3/fulcro-card
   {::ct.fulcro3/root u.index-accounts/IndexAccounts
    ::ct.fulcro3/initial-state
    (fn []
      {::u.index-accounts/accounts
       (map
        (fn [account]
          (-> account
              (assoc ::m.accounts/user [{::m.users/name "foo"
                                         ::m.users/id   m.users/default-username}])
              (assoc ::m.accounts/currency [{::m.currencies/name "Sats"
                                             ::m.currencies/id   "sats"}])))
        (vals sample/account-map))})}))

(ws/defcard IndexAccountsPage
  {::wsm/align       {:flex 1}
   ::wsm/card-height 12
   ::wsm/card-width  4}
  (ct.fulcro3/fulcro-card
   {::ct.fulcro3/root u.index-accounts/IndexAccountsPage
    ::ct.fulcro3/initial-state
    (fn []
      {::u.index-accounts/user-accounts
       {::u.user-accounts/accounts
        {::u.user-accounts/accounts (map sample/account-map [1 2])}
        ::u.user-accounts/form          {}
        ::u.user-accounts/toggle-button {}}})}))
