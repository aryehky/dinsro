(ns dinsro.queries.wallet-addresses
  (:require
   [clojure.spec.alpha :as s]
   [com.fulcrologic.guardrails.core :refer [>defn ? =>]]
   [com.fulcrologic.rad.ids :refer [new-uuid]]
   [dinsro.components.xtdb :as c.xtdb]
   [dinsro.model.wallet-addresses :as m.wallet-addresses]
   [dinsro.model.wallets :as m.wallets]
   [dinsro.specs]
   [xtdb.api :as xt]))

(>defn index-ids
  []
  [=> (s/coll-of ::m.wallet-addresses/id)]
  (let [db    (c.xtdb/main-db)
        query '{:find  [?e]
                :where [[?e ::m.wallet-addresses/id _]]}]
    (map first (xt/q db query))))

(>defn read-record
  [id]
  [:xt/id => (? ::m.wallet-addresses/item)]
  (let [db     (c.xtdb/main-db)
        record (xt/pull db '[*] id)]
    (when (get record ::m.wallet-addresses/id)
      (dissoc record :xt/id))))

(>defn create-record
  [params]
  [::m.wallet-addresses/params => ::m.wallet-addresses/id]
  (let [node            (c.xtdb/main-node)
        id              (new-uuid)
        prepared-params (-> params
                            (assoc ::m.wallet-addresses/id id)
                            (assoc :xt/id id))]
    (xt/await-tx node (xt/submit-tx node [[::xt/put prepared-params]]))
    id))

(>defn index-records
  []
  [=> (s/coll-of ::m.wallet-addresses/item)]
  (map read-record (index-ids)))

(>defn find-by-wallet
  [wallet-id]
  [::m.wallets/id => (s/coll-of ::m.wallet-addresses/id)]
  (let [db    (c.xtdb/main-db)
        query '{:find  [?address-id]
                :in    [?wallet-id]
                :where [[?address-id ::m.wallet-addresses/wallet ?wallet-id]]}]
    (map first (xt/q db query wallet-id))))
