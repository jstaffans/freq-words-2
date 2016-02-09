(ns freq-words-2.handlers
  (:require [re-frame.core :refer [register-handler]]
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
  :randomise
  (fn [db [_ random-order?]]
    (assoc-in db [:options :random-order?] random-order?)))

(register-handler
  :start-game
  (fn [db [_ group]]
    (assoc-in db [:progress :words]
      (let [words (nth word-groups group)]
        (if (-> db :options :random-order?)
          (shuffle words)
          words)))))

(register-handler
  :continue
  (fn [db _]
    (update-in db [:progress :words] rest)))
