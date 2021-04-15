(ns dinsro.ui.forms.registration-test
  (:require
   [dinsro.model.users :as m.users]
   [dinsro.specs :as ds]
   [dinsro.ui.forms.registration :as u.f.registration]
   [nubank.workspaces.card-types.fulcro3 :as ct.fulcro3]
   [nubank.workspaces.core :as ws]
   [nubank.workspaces.model :as wsm]
   [taoensso.timbre :as timbre]))

(ws/defcard RegistrationForm
  {::wsm/card-width  2
   ::wsm/card-height 11}
  (ct.fulcro3/fulcro-card
   {::ct.fulcro3/initial-state
    (fn []
      #::u.f.registration
       {:name             (ds/gen-key ::m.users/name)
        :email            (ds/gen-key ::m.users/email)
        :password         (ds/gen-key ::m.users/password)
        :confirm-password (ds/gen-key ::m.users/password)})
    ::ct.fulcro3/root       u.f.registration/RegistrationForm}))
