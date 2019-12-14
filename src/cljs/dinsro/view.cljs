(ns dinsro.view
  (:require [com.smxemail.re-frame-document-fx]
            [dinsro.components.navbar :refer [navbar]]
            [dinsro.routing :as r]
            [kee-frame.core :as kf]
            [taoensso.timbre :as timbre]))

(defn root-component []
  [:<>
   [navbar]
   (->> (-> (->> r/mappings
                 (map identity)
                 (into [])
                 (reduce concat []))
            (concat [nil [:div "Not Found"]]))
        (into [kf/switch-route (fn [route] (get-in route [:data :name]))]))])
