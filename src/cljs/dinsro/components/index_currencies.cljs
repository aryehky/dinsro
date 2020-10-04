(ns dinsro.components.index-currencies
  (:require
   [dinsro.components.buttons :as c.buttons]
   [dinsro.components.debug :as c.debug]
   [dinsro.components.links :as c.links]
   [dinsro.translations :refer [tr]]
   [taoensso.timbre :as timbre]))

(defn index-currency-line
  [store currency]
  (let [{:keys [db/id]} currency]
    [:tr
     [:td [c.links/currency-link store id]]
     (c.debug/hide store [:td [c.buttons/delete-currency store currency]])]))

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
