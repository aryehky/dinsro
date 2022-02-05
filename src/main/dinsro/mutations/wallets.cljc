(ns dinsro.mutations.wallets
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   #?(:cljs [com.fulcrologic.fulcro.mutations :as fm])
   #?(:cljs [com.fulcrologic.rad.form :as form])
   [com.wsscode.pathom.connect :as pc]
   #?(:clj [dinsro.actions.authentication :as a.authentication])
   #?(:clj [dinsro.actions.wallets :as a.wallets])
   [dinsro.model.wallets :as m.wallets]
   #?(:cljs [taoensso.timbre :as log])))

(comment ::pc/_ ::m.wallets/_)

(defsc CreationResponse
  [_this _props]
  {:query [:status :id]})

#?(:clj
   (pc/defmutation create!
     [env props]
     {::pc/params #{::m.wallets/id}
      ::pc/output [:status]}
     (let [user-id (a.authentication/get-user-id env)
           props   (assoc props ::m.wallets/user user-id)
           id (a.wallets/create! props)]
       {:status :ok
        :id     id}))

   :cljs
   (fm/defmutation create! [_props]
     (action [_env] true)
     (remote [env] (fm/returning env CreationResponse))
     (ok-action [{:keys [app component] :as env}]
       (let [body (get-in env [:result :body])
             response (get body `create!)]
         (log/spy :info response)
         (let [{:keys [id]} response
               target-component (comp/registry-key->class :dinsro.ui.wallets/WalletForm)]
           (form/mark-all-complete! component)
           (form/view! app target-component id))
         {}))))

#?(:clj (def resolvers [create!]))
