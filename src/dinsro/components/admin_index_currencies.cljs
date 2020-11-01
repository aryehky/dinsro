(ns dinsro.components.admin-index-currencies
  (:require
   [clojure.spec.alpha :as s]
   [dinsro.components :as c]
   [dinsro.components.buttons :as c.buttons]
   [dinsro.components.debug :as c.debug]
   [dinsro.components.forms.create-currency :as c.f.create-currency]
   [dinsro.components.index-currencies :as c.index-currencies]
   [dinsro.components.links :as c.links]
   [dinsro.specs.currencies :as s.currencies]
   [dinsro.events.currencies :as e.currencies]
   [dinsro.events.forms.create-currency :as e.f.create-currency]
   [dinsro.store :as st]
   [dinsro.translations :refer [tr]]
   [taoensso.timbre :as timbre]))

(defn index-currency-line
  [store currency]
  (let [{:keys [db/id]} currency]
    [:tr
     [:td [c.links/currency-link store id]]
     (c.debug/hide store [:td [c.buttons/delete-currency store currency]])]))

(s/fdef index-currency-line
  :args (s/cat :currency ::s.currencies/item)
  :ret vector?)

(defn index-currencies
  [store currencies]
  (if-not (seq currencies)
    [:div (tr [:no-currencies])]
    [:table
     [:thead>tr
      [:th (tr [:name-label])]
      (c.debug/hide store [:th "Buttons"])]
     (into
      [:tbody]
      (for [{:keys [db/id] :as currency} currencies]
        ^{:key id} [index-currency-line store currency]))]))

(s/fdef index-currencies
  :args (s/cat :currencies (s/coll-of ::s.currencies/item))
  :ret vector?)

(defn section-inner
  [store currencies]
  [:div.box
   [:h1
    (tr [:index-currencies "Index Currencies"])
    [c/show-form-button store ::e.f.create-currency/shown?]]
   [c.f.create-currency/form store]
   [:hr]
   (when currencies
     [c.index-currencies/index-currencies store currencies])])

(defn section
  [store]
  (let [currencies @(st/subscribe store [::e.currencies/items])]
    [section-inner store currencies]))
