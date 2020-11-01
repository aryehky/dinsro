(ns dinsro.components.forms.create-transaction-test
  (:require
   [cljs.pprint :as p]
   [clojure.spec.alpha :as s]
   [dinsro.cards :refer-macros [defcard-rg]]
   [dinsro.components.forms.create-transaction :as c.f.create-transaction]
   [dinsro.events.accounts :as e.accounts]
   [dinsro.events.debug :as e.debug]
   [dinsro.events.forms.create-transaction :as e.f.create-transaction]
   [dinsro.specs :as ds]
   [dinsro.specs.accounts :as s.accounts]
   [dinsro.specs.transactions :as s.transactions]
   [dinsro.store :as st]
   [dinsro.store.mock :refer [mock-store]]
   [dinsro.translations :refer [tr]]
   [taoensso.timbre :as timbre]))

(def accounts (ds/gen-key (s/coll-of ::s.accounts/item :count 3)))
(def transactions (ds/gen-key (s/coll-of ::s.transactions/item :count 3)))

(defn test-store
  []
  (let [store (doto (mock-store)
                e.accounts/init-handlers!
                e.debug/init-handlers!
                e.f.create-transaction/init-handlers!)]
    store))

(let [store (test-store)]
  (st/dispatch store [::e.accounts/do-fetch-index-success {:items accounts}])
  (st/dispatch store [::e.f.create-transaction/set-shown? true])

  (defcard-rg form-data-card
    [:pre (with-out-str (p/pprint @(st/subscribe store [::e.f.create-transaction/form-data])))])

  (defcard-rg create-transaction-card
    [c.f.create-transaction/form store]))
