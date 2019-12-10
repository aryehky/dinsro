(ns dinsro.views.index-transactions
  (:require [day8.re-frame.tracing :refer-macros [fn-traced]]
            [dinsro.components.buttons :as c.buttons]
            [dinsro.components.forms.create-transaction :as c.f.create-transaction]
            [dinsro.components.index-transactions :refer [index-transactions]]
            [dinsro.spec.transactions :as s.transactions]
            [dinsro.translations :refer [tr]]
            [kee-frame.core :as kf]
            [re-frame.core :as rf]
            [taoensso.timbre :as timbre]))

(defn load-buttons
  []
  [:div.box
   [c.buttons/fetch-transactions]
   [c.buttons/fetch-accounts]
   [c.buttons/fetch-currencies]
   [c.buttons/toggle-debug]])

(def example-transaction
  {:db/id 1
   ::s.transactions/value 130000
   ::s.transactions/currency {:db/id 53}
   ::s.transactions/account {:db/id 12}})

(defn page
  []
  [:section.section>div.container>div.content
   [load-buttons]
   (let [transactions [example-transaction]]
     [:div.box
      [:h1 "Index Transactions"]
      [c.f.create-transaction/create-transaction-form]
      [:hr]
      [index-transactions transactions]])])
