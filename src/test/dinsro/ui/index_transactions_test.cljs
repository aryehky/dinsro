(ns dinsro.ui.index-transactions-test
  (:require
   [dinsro.sample :as sample]
   [dinsro.translations :refer [tr]]
   [dinsro.ui.index-transactions :as u.index-transactions]
   [nubank.workspaces.card-types.fulcro3 :as ct.fulcro3]
   [nubank.workspaces.core :as ws]
   [nubank.workspaces.model :as wsm]))

(ws/defcard IndexTransactions
  {::wsm/align       {:flex 1}
   ::wsm/card-height 11
   ::wsm/card-width  5}
  (ct.fulcro3/fulcro-card
   {::ct.fulcro3/root       u.index-transactions/IndexTransactions
    ::ct.fulcro3/initial-state
    (fn []
      {::u.index-transactions/transactions
       (map sample/transaction-map [1 2])})}))

(ws/defcard IndexTransactionsPage
  {::wsm/align       {:flex 1}
   ::wsm/card-height 15
   ::wsm/card-width  5}
  (ct.fulcro3/fulcro-card
   {::ct.fulcro3/root u.index-transactions/IndexTransactionsPage
    ::ct.fulcro3/initial-state
    (fn []
      {:page/id                             ::u.index-transactions/page
       ::u.index-transactions/form          {}
       ::u.index-transactions/toggle-button {}
       ::u.index-transactions/transactions
       {::u.index-transactions/transactions (vals sample/transaction-map)}})}))
