(ns dinsro.queries.ln.remote-nodes
  (:require
   [clojure.spec.alpha :as s]
   [com.fulcrologic.guardrails.core :refer [>defn ? =>]]
   [com.fulcrologic.rad.ids :refer [new-uuid]]
   [dinsro.components.xtdb :as c.xtdb]
   [dinsro.model.ln.remote-nodes :as m.ln.remote-nodes]
   [dinsro.model.ln.nodes :as m.ln.nodes]
   [dinsro.specs]
   [lambdaisland.glogc :as log]
   [xtdb.api :as xt]))

(>defn index-ids
  []
  [=> (s/coll-of ::m.ln.remote-nodes/id)]
  (let [db    (c.xtdb/main-db)
        query '{:find  [?e]
                :where [[?e ::m.ln.remote-nodes/id _]]}]
    (map first (xt/q db query))))

(>defn read-record
  [id]
  [:xt/id => (? ::m.ln.remote-nodes/item)]
  (let [db     (c.xtdb/main-db)
        record (xt/pull db '[*] id)]
    (when (get record ::m.ln.remote-nodes/id)
      (dissoc record :xt/id))))

(>defn create-record
  [params]
  [::m.ln.remote-nodes/params => ::m.ln.remote-nodes/id]
  (let [node            (c.xtdb/main-node)
        id              (new-uuid)
        prepared-params (-> params
                            (assoc ::m.ln.remote-nodes/id id)
                            (assoc :xt/id id))]
    (xt/await-tx node (xt/submit-tx node [[::xt/put prepared-params]]))
    id))

(>defn index-records
  []
  [=> (s/coll-of ::m.ln.remote-nodes/item)]
  (map read-record (index-ids)))

(>defn find-ids-by-node
  [node-id]
  [::m.ln.nodes/id => (s/coll-of ::m.ln.remote-nodes/id)]
  (let [db    (c.xtdb/main-db)
        query '{:find  [?channel-id]
                :in    [?node-id]
                :where [[?channel-id ::m.ln.remote-nodes/node ?node-id]]}]
    (map first (xt/q db query node-id))))

(>defn find-by-network-and-pubkey
  [network-id pubkey]
  [::m.ln.remote-nodes/network ::m.ln.remote-nodes/pubkey => (? ::m.ln.remote-nodes/id)]
  (let [db    (c.xtdb/main-db)
        query '{:find  [?remote-node-id]
                :in    [[?network-id ?pubkey]]
                :where [[?remote-node-id ::m.ln.remote-nodes/pubkey ?pubkey]
                        [?remote-node-id ::m.ln.remote-nodes/network ?network-id]]}
        id (ffirst (xt/q db query [network-id pubkey]))]
    (log/info :find-by-network-and-pubkey/finished {:id id :network-id network-id :pubkey pubkey})
    id))

(>defn find-channel
  [node-id channel-point]
  [::m.ln.remote-nodes/node ::m.ln.remote-nodes/channel-point => (? ::m.ln.remote-nodes/id)]
  (let [db    (c.xtdb/main-db)
        query '{:find  [?channel-id]
                :in    [[?node-id ?channel-point]]
                :where [[?channel-id ::m.ln.remote-nodes/node ?node-id]
                        [?channel-id ::m.ln.remote-nodes/channel-point ?channel-point]]}]
    (ffirst (xt/q db query [node-id channel-point]))))

(>defn delete!
  [id]
  [::m.ln.remote-nodes/id => nil?]
  (let [node (c.xtdb/main-node)]
    (xt/await-tx node (xt/submit-tx node [[::xt/delete id]])))
  nil)

(>defn update!
  [params]
  [::m.ln.remote-nodes/item => ::m.ln.remote-nodes/id]
  (if-let [id (::m.ln.remote-nodes/id params)]
    (let [node   (c.xtdb/main-node)
          params (assoc params :xt/id id)]
      (xt/await-tx node (xt/submit-tx node [[::xt/put params]]))
      id)
    (throw (RuntimeException. "Failed to find id"))))
