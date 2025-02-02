(ns dinsro.ui.users
  (:require
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.rad.control :as control]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [dinsro.joins.users :as j.users]
   [dinsro.model.users :as m.users]
   [dinsro.ui.links :as u.links]))

(def override-form true)

(form/defsc-form UserForm
  [this {::m.users/keys [name] :as props}]
  {fo/id           m.users/id
   fo/attributes   [m.users/name
                    j.users/accounts
                    j.users/categories
                    j.users/ln-nodes
                    j.users/transactions]
   fo/cancel-route ["users"]
   fo/field-styles {::m.users/accounts     :account-table
                    ::m.users/categories   :link-list
                    ::m.users/ln-nodes     :link-list
                    ::m.users/transactions :link-list}
   fo/route-prefix "user"
   fo/subforms     {::m.users/accounts     {fo/ui u.links/AccountLinkForm}
                    ::m.users/categories   {fo/ui u.links/CategoryLinkForm}
                    ::m.users/ln-nodes     {fo/ui u.links/NodeLinkForm}
                    ::m.users/transactions {fo/ui u.links/TransactionLinkForm}}
   fo/title        "User"}
  (if override-form
    (form/render-layout this props)
    (dom/div {}
      (dom/p {} name))))

(form/defsc-form AdminUserForm
  [_this _props]
  {fo/id           m.users/id
   fo/attributes   [m.users/name
                    m.users/role
                    m.users/password]
   fo/cancel-route ["admin"]
   fo/route-prefix "admin-user"
   fo/title        "Admin User"})

(report/defsc-report UsersReport
  [_this _props]
  {ro/form-links       {::m.users/name UserForm}
   ro/columns          [m.users/name]
   ro/source-attribute ::m.users/index
   ro/title            "Users"
   ro/route            "users"
   ro/row-pk           m.users/id
   ro/run-on-mount?    true})

(report/defsc-report AdminIndexUsersReport
  [_this _props]
  {ro/columns          [m.users/name m.users/role]
   ro/controls         {::new-user {:label  "New User"
                                    :type   :button
                                    :action (fn [this] (form/create! this AdminUserForm))}
                        ::refresh  {:label  "Refresh"
                                    :type   :button
                                    :action (fn [this] (control/run! this))}}
   ro/form-links       {::m.users/name AdminUserForm}
   ro/source-attribute ::m.users/index
   ro/title            "Admin Users"
   ro/row-pk           m.users/id
   ro/route            "users"
   ro/run-on-mount?    true})

(def ui-admin-index-users (comp/factory AdminIndexUsersReport))
