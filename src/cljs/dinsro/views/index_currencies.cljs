(ns dinsro.views.index-currencies
  (:require
   [clojure.spec.alpha :as s]
   [dinsro.components :as c]
   [dinsro.components.buttons :as c.buttons]
   [dinsro.components.debug :as c.debug]
   [dinsro.components.forms.create-currency :as c.f.create-currency]
   [dinsro.components.index-currencies :as c.index-currencies]
   [dinsro.events.currencies :as e.currencies]
   [dinsro.events.forms.create-currency :as e.f.create-currency]
   [dinsro.store :as st]
   [dinsro.translations :refer [tr]]
   [kee-frame.core :as kf]
   [reitit.core :as rc]))

(defn init-page
  [_ _]
  {:dispatch [::e.currencies/do-fetch-index]
   :document/title "Index Currencies"})

(kf/reg-event-fx ::init-page init-page)

(kf/reg-controller
 ::page-controller
 {:params (c/filter-page :index-currencies-page)
  :start  [::init-page]})

(defn loading-buttons
  []
  [:div.box
   [c.buttons/fetch-currencies]])

(defn page
  [store _match]
  (let [currencies @(st/subscribe store [::e.currencies/items])]
    [:section.section>div.container>div.content
     (c.debug/hide [loading-buttons])
     [:div.box
      [:h1
       (tr [:index-currencies "Index Currencies"])
       [c/show-form-button ::e.f.create-currency/shown?]]
      [c.f.create-currency/form]
      [:hr]
      (when currencies
        [c.index-currencies/index-currencies currencies])]]))

(s/fdef page
  :args (s/cat :store #(instance? st/Store %)
               :match #(instance? rc/Match %))
  :ret vector?)
