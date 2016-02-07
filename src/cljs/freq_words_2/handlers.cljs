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
  :start-game
  (fn [db [_ group]]
    (assoc-in db [:progress :words] (nth word-groups group))))
