(ns dinsro.views.settings
  (:require
   [clojure.spec.alpha :as s]
   [dinsro.components :as c]
   [dinsro.components.forms.settings :as c.f.settings]
   [dinsro.store :as st]
   [dinsro.translations :refer [tr]]
   [kee-frame.core :as kf]
   [reitit.core :as rc]
   [taoensso.timbre :as timbre]))

(defn init-page
  [_ _]
  {:document/title "Settings"})

(kf/reg-event-fx ::init-page init-page)

(kf/reg-controller
 ::page
 {:params (c/filter-page :settings-page)
  :start [::init-page]})

(defn page
  [_store _match]
  [:section.section>div.container>div.content
   [:div.box
    [:h1 "Settings Page"]
    [c.f.settings/form]]])

(s/fdef page
  :args (s/cat :store #(instance? st/Store %)
               :match #(instance? rc/Match %))
  :ret vector?)
