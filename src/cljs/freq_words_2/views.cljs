(ns freq-words-2.views
  (:require-macros [freq-words-2.macros :refer [fore]])
  (:require [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :as str]
            [freq-words-2.words :refer [word-groups]]))

(defn preview-words
  [words]
  (str (str/join ", " words) " …"))

(defn word-group
  [{:keys [group-index group]}]
  (let [class (str "word-group group-" group-index)]
    [:div.col-md-4.word-col {:key group-index}
     [:a {:class-name class
          :key        group-index
          :tab-index  (inc group-index)
          :href       (freq-words-2.routes/group-path {:id group-index})}
      [:div.preview (str group-index ". " (preview-words (take 3 group)))]]]))

(defn word-group-row
  [groups-in-row]
  [:div {:class "row" :key (-> groups-in-row first :group-index)}
   (fore [group groups-in-row]
     (word-group group))
   [:div {:style {:clear "left"}}]])

(defn group-selection
  [word-groups]
  [:div
   [:nav.navbar.navbar-default.navbar-fixed-top
    [:div.container.container-header.vertical-align
     [:div.col-md-8.header-title "Välj grupp"]]]
   [:div.container.container-groups
    (fore [group-row (->> word-groups
                       (map-indexed (fn [i group] (assoc {} :group-index (inc i) :group group)))
                       (partition-all 3))]
      (word-group-row group-row))]])

(defn group-intro
  [group-index words]
  [:div
   [:div.intro (str "Grupp " group-index ": " (preview-words (take 3 words)))]
   [:div.controls
    [:div.start [:i.fa.fa-play-circle {:on-click #(dispatch [:start-game group-index])}]]
    [:div.options
     [:div.option
      [:input {:id "random-order" :type "checkbox"}]
      [:label {:for "random-order"} "Slumpmässig ordföljd"]]
     [:div.option
      [:input {:id "keep-time" :type "checkbox"}]
      [:label {:for "keep-time"} "Tidtagning"]]
     [:div.option.option-cancel
      [:a {:href (freq-words-2.routes/root-path)} "Tillbaka"]]]]])

(defn group
  [group-index words]
  (let [current-words (subscribe [:current-words])]
    (fn []
      [:div {:class-name (str "container-game group-" (inc group-index))}
       (if-not @current-words
         [group-intro group-index words]
         (first @current-words))])))



(defn app
  []
  (let [selected-group (subscribe [:selected-group])]
    (fn []
      (if-not @selected-group
        [group-selection word-groups]
        [group @selected-group (nth word-groups @selected-group)]))))
