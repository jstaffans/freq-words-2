(ns freq-words-2.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
  :selected-group
  (fn [db _]
    (reaction
      (when-let [group (:selected-group @db)]
        (dec group)))))

(register-sub
  :current-words
  (fn [db _]
    (reaction (get-in @db [:progress :words]))))


