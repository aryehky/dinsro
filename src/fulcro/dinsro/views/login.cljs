(ns dinsro.views.login
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]
   [dinsro.translations :refer [tr]]
   [dinsro.ui.bulma :as bulma]
   [taoensso.timbre :as timbre]))

(defsc LoginPage
  [_this {::keys [foo]}]
  {:query [::foo]
   :route-segment ["login"]
   :ident (fn [_] [:page/id ::page])
   :initial-state {::foo "bar"}}
  (bulma/section
   (bulma/container
    (bulma/content
     (dom/h1 "Login")
     (dom/p (str "foo: " foo))))))
