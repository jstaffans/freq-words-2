(ns freq-words-2.handlers
  (:require [re-frame.core :refer [register-handler]]
            [cljs-time.core :as ct]
            [freq-words-2.words :refer [word-groups]]
            [freq-words-2.db :refer [default-state]]))

(register-handler
  :initialise-db
  (fn [_ _]
    default-state))

(register-handler
  :select-group
  (fn [db [_ group]]
    (assoc db :selected-group group :progress {})))

(register-handler
  :update-options
  (fn [db [_ k v]]
    (update-in db [:options] #(assoc % k v))))

(register-handler
  :start-game
  (fn [db [_ group]]
    (-> db
      (assoc-in [:progress :words] (let [words (nth word-groups group)]
                                     (if (-> db :options :random-order?)
                                       (shuffle words)
                                       words)))
      (assoc-in [:progress :start] (ct/now)))))

(register-handler
  :continue
  (fn [db _]
    (update-in db [:progress :words] rest)))
