(ns freq-words-2.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [cljs-time.core :as ct]))

(register-sub
  :selected-group
  (fn [db _]
    (reaction
      (when-let [group (:selected-group @db)]
        (dec group)))))

(register-sub
  :options
  (fn [db _]
    (reaction (:options @db))))

(defn- to-mins-secs
  [secs]
  {:minutes (quot secs 60) :seconds (rem secs 60)})

(register-sub
  :current-time
  (fn [db _]
    (reaction
      (when (and (-> @db :options :timer?) (-> @db :progress :start))
        (let [secs (ct/in-seconds (ct/interval (-> @db :progress :start) (ct/now)))]
          (to-mins-secs secs))))))

(register-sub
  :current-words
  (fn [db _]
    (reaction (get-in @db [:progress :words]))))


