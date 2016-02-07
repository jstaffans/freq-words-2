(ns freq-words-2.core
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch]]
            [freq-words-2.routes :as routes]
            [freq-words-2.handlers]
            [freq-words-2.subs]
            [freq-words-2.views]))

(defn mount-root
  []
  (reagent/render [freq-words-2.views/app] (.getElementById js/document "app")))

(defn ^:export main []
  (dispatch [:initialise-db])
  (routes/init)
  (mount-root))
