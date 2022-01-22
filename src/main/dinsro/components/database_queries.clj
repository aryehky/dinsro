(ns dinsro.components.database-queries
  (:require
   [xtdb.api :as xt]
   [roterski.fulcro.rad.database-adapters.xtdb-options :as co]
   [taoensso.encore :as enc]
   [taoensso.timbre :as log]))

(defn get-my-accounts-
  [db user-id]
  (let [query    '{:find  [?uuid]
                   :in    [?user-id]
                   :where [[?uuid :dinsro.model.accounts/user ?user-id]]}
        response (xt/q db query user-id)]
    (mapv (fn [[id]] {:dinsro.model.accounts/id id}) response)))

(defn get-my-accounts
  [env user-id _params]
  (if-let [db (some-> (get-in env [co/databases :production]) deref)]
    (get-my-accounts- db user-id)
    (log/error "No database atom for production schema!")))

(defn get-login-info
  "Get the account name, time zone, and password info via a username (email)."
  [env username]
  (enc/if-let [db (some-> (get-in env [co/databases :production]) deref)]
    (let [query '{:find  [(pull ?user-id
                                [:dinsro.model.users/id
                                 :dinsro.model.users/name
                                 {:time-zone/zone-id [:xt/id]}
                                 :dinsro.model.users/hashed-value
                                 :dinsro.model.users/salt
                                 :dinsro.model.users/iterations])]
                  :in    [?username]
                  :where [[?user-id :dinsro.model.users/name ?username]]}]
      (ffirst (xt/q db query username)))))