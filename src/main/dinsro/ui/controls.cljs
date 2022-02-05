(ns dinsro.ui.controls
  (:require
   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.rendering.semantic-ui.field :refer [render-field-factory]]
   [com.fulcrologic.rad.rendering.semantic-ui.semantic-ui-controls :as sui]
   [dinsro.model.accounts :as m.accounts]
   [dinsro.model.categories :as m.categories]
   [dinsro.model.core-block :as m.core-block]
   [dinsro.model.core-nodes :as m.core-nodes]
   [dinsro.model.core-tx :as m.core-tx]
   [dinsro.model.currencies :as m.currencies]
   [dinsro.model.ln-channels :as m.ln-channels]
   [dinsro.model.ln-invoices :as m.ln-invoices]
   [dinsro.model.ln-payments :as m.ln-payments]
   [dinsro.model.ln-payreqs :as m.ln-payreqs]
   [dinsro.model.ln-peers :as m.ln-peers]
   [dinsro.model.ln-nodes :as m.ln-nodes]
   [dinsro.model.ln-transactions :as m.ln-tx]
   [dinsro.model.rate-sources :as m.rate-sources]
   [dinsro.model.transactions :as m.transactions]
   [dinsro.model.users :as m.users]
   [dinsro.model.wallets :as m.wallets]
   [dinsro.model.wallet-addresses :as m.wallet-addresses]
   [dinsro.translations :refer [tr]]
   [dinsro.ui.accounts :as u.accounts]
   [dinsro.ui.core-block :as u.core-block]
   [dinsro.ui.core-tx-in :as u.core-tx-in]
   [dinsro.ui.core-tx-out :as u.core-tx-out]
   [dinsro.ui.links :as u.links]
   [dinsro.ui.ln-channels :as u.ln-channels]
   [dinsro.ui.ln-invoices :as u.ln-invoices]
   [dinsro.ui.ln-payments :as u.ln-payments]
   [dinsro.ui.ln-payreqs :as u.ln-payreqs]
   [dinsro.ui.ln-peers :as u.ln-peers]
   [dinsro.ui.ln-transactions :as u.ln-tx]
   [dinsro.ui.rates :as u.rates]
   ["victory" :as victory]))

(def ui-victory-bar (interop/react-factory victory/VictoryBar))
(def ui-victory-chart (interop/react-factory victory/VictoryChart))
(def ui-victory-line (interop/react-factory victory/VictoryLine))
(def ui-victory-axis  (interop/react-factory victory/VictoryAxis))
(def ui-victory-tooltip  (interop/react-factory victory/VictoryTooltip))

(defn link-control
  [{:keys [value] :as env} _attribute]
  (let [{account-id     ::m.accounts/id
         address-id     ::m.wallet-addresses/id
         category-id    ::m.categories/id
         block-id       ::m.core-block/id
         core-node-id   ::m.core-nodes/id
         core-tx-id     ::m.core-tx/id
         currency-id    ::m.currencies/id
         channel-id     ::m.ln-channels/id
         invoice-id     ::m.ln-invoices/id
         ln-tx-id       ::m.ln-tx/id
         payreq-id      ::m.ln-payreqs/id
         payment-id     ::m.ln-payments/id
         peer-id        ::m.ln-peers/id
         node-id        ::m.ln-nodes/id
         source-id      ::m.rate-sources/id
         transaction-id ::m.transactions/id
         user-id        ::m.users/id
         wallet-id ::m.wallets/id} value]
    (or
     (when account-id (u.links/ui-account-link value))
     (when block-id (u.links/ui-block-link value))
     (when core-tx-id (u.links/ui-core-tx-link value))
     (when category-id (u.links/ui-category-link value))
     (when channel-id (u.links/ui-channel-link value))
     (when core-node-id (u.links/ui-core-node-link value))
     (when currency-id (u.links/ui-currency-link value))
     (when invoice-id (u.links/ui-invoice-link value))
     (when node-id (u.links/ui-node-link value))
     (when ln-tx-id (u.links/ui-ln-tx-link value))
     (when payment-id (u.links/ui-payment-link value))
     (when payreq-id (u.links/ui-payreq-link value))
     (when peer-id (u.links/ui-ln-peer-link value))
     (when source-id (u.links/ui-rate-source-link value))
     (when transaction-id (u.links/ui-transaction-link value))
     (when user-id (u.links/ui-user-link value))
     (when wallet-id (u.links/ui-wallet-link value))
     (when address-id (u.links/ui-wallet-address-link value))
     (dom/div (merge env {}) (str "link control: " value)))))

(def render-link-control (render-field-factory link-control))

(defsc LinkControlItem
  [_this {:keys [attribute item]}]
  (dom/div {}
    (link-control {:value item} attribute)))

(def ui-link-control-list-item
  (comp/factory
   LinkControlItem
   {:keyfn (fn [i]
             (let [ident (get-in i
                                 [:item
                                  :com.fulcrologic.fulcro.algorithms.form-state/config
                                  :com.fulcrologic.fulcro.algorithms.form-state/id])]
               (second ident)))}))

(defn link-list-control
  [{:keys [value]} attribute]
  (dom/div {}
    (for [item value] (ui-link-control-list-item {:attribute attribute :item item}))))

(def render-link-list-control (render-field-factory link-list-control))

(defn link-subform-control
  [{:keys [value]} attribute]
  (dom/div {}
    (dom/ul {}
      (for [item value]
        (dom/li {}
          (link-control {:value item} attribute))))))

(def render-link-subform-control (render-field-factory link-subform-control))

(defn ref-control
  [{:keys [value]} _attribute]
  (dom/div :.ui
    (dom/div {} "default ref control: ")
    (dom/pre
     {}
     (dom/code {} (pr-str value)))))

(def render-ref (render-field-factory ref-control))

(defn date-control
  [{:keys [value] :as _env} _attribute]
  (dom/div {} (str value)))

(def render-date (render-field-factory date-control))

(defn uuid-control
  [{:keys [value]} _attribute]
  (dom/div {} (str "uuid control" value)))

(def render-uuid (render-field-factory uuid-control))

(defn user-selector-control
  [{:keys [value]} _attribute]
  (dom/div {} (str "user selector control" value)))

(def render-user-selector (render-field-factory user-selector-control))

(defn control-type
  [controls type style control]
  (assoc-in controls [::form/type->style->control type style] control))

(defn rate-chart-control
  [{:keys [value]} _attribute]
  (dom/div {}
    (ui-victory-chart
     {:domainPadding {:x 50}}
     (ui-victory-line
      {:data   value
       :style  (clj->js {:data {:stroke "#c43a31"}})
       :labels (fn [v] (comp/isoget-in v ["date" "rate"]))
       :x      "date"
       :y      "rate"}))))

(def render-rate-chart-control (render-field-factory rate-chart-control))

(defn all-controls
  []
  (-> sui/all-controls
      (control-type :ref  :account-table    u.accounts/render-ref-table)
      (control-type :ref  :default          render-ref)
      (control-type :ref  :link             render-link-control)
      (control-type :ref  :link-list        render-link-list-control)
      (control-type :ref  :link-subform     render-link-subform-control)
      (control-type :ref  :core-block-table u.core-block/render-ref-row)
      (control-type :ref  :ln-tx-row        u.ln-tx/render-ref-ln-tx-row)
      (control-type :ref  :ln-channels-row  u.ln-channels/render-ref-row)
      (control-type :ref  :ln-payments-row  u.ln-payments/render-ref-row)
      (control-type :ref  :ln-payreqs-row   u.ln-payreqs/render-ref-ln-payreq-row)
      (control-type :ref  :ln-peer-row      u.ln-peers/render-ref-ln-peer-row)
      (control-type :ref  :ln-invoice-row   u.ln-invoices/render-ref-ln-invoice-row)
      (control-type :ref  :tx-in-table      u.core-tx-in/render-ref-row)
      (control-type :ref  :tx-out-table     u.core-tx-out/render-ref-row)
      (control-type :ref  :rate-chart       render-rate-chart-control)
      (control-type :ref  :rate-table       u.rates/render-ref-table)
      (control-type :ref  :user-selector    render-user-selector)
      (control-type :uuid :default          render-uuid)
      (control-type :date :default          render-date)))
