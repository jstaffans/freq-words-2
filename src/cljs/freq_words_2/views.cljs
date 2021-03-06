(ns freq-words-2.views
  (:require-macros [freq-words-2.macros :refer [fore]])
  (:require [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :as str]
            [freq-words-2.words :refer [word-groups]]
            [reagent.core :as reagent]))

(def css-transition-group
  (reagent/adapt-react-class js/React.addons.CSSTransitionGroup))

(defn- preview-words
  [words]
  (str (str/join ", " words) " …"))

(defn- word-group
  [{:keys [group-index group]}]
  (let [class (str "word-group group-" group-index)]
    [:div.col-md-4.word-col {:key group-index}
     [:a {:class-name class
          :key        group-index
          :tab-index  (inc group-index)
          :href       (freq-words-2.routes/group-path {:id group-index})}
      [:div.preview (str group-index ". " (preview-words (take 3 group)))]]]))

(defn- word-group-row
  [groups-in-row]
  [:div {:class "row" :key (-> groups-in-row first :group-index)}
   (fore [group groups-in-row]
     (word-group group))
   [:div {:style {:clear "left"}}]])

(defn- group-selection
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

(defn- checked?
  [event]
  (-> event .-target .-checked))

(defn- group-intro
  [group-index words]
  (let [options (subscribe [:options])]
    (fn []
      [:div
       [:div.intro (str "Grupp " (inc group-index) ":")]
       [:div.subintro (preview-words (take 3 words))]
       [:div.controls
        [:div.start [:i.fa.fa-play-circle {:on-click #(dispatch [:start-game group-index])}]]
        [:div.options
         [:div.option
          [:input {:id        "random-order"
                   :type      "checkbox"
                   :checked   (:random-order? @options)
                   :on-change #(dispatch [:update-options :random-order? (checked? %)])}]
          [:label {:for "random-order"} "Slumpmässig ordföljd"]]
         [:div.option
          [:input {:id        "keep-time"
                   :type      "checkbox"
                   :checked   (:timer? @options)
                   :on-change #(dispatch [:update-options :timer? (checked? %)])}]
          [:label {:for "keep-time"} "Tidtagning"]]
         [:div.option.option-cancel
          [:a {:href (freq-words-2.routes/root-path)} "Tillbaka"]]]]])))

(defn- show-word
  [word]
  [:div.controls
   [css-transition-group
    {:transition-name   "word"
     :transition-appear true}
    [:div.word {:key word} word]]
   [:div.continue.fade-in-once [:i.fa.fa-play-circle {:on-click #(dispatch [:continue])}]]
   [:div [:a.fade-in-once {:href (freq-words-2.routes/root-path)} "Tillbaka"]]])

(defn- group-finished
  []
  (let [time (subscribe [:current-time])]
    [:div.controls
     [:div.done "Gruppen avklarad!"]
     (when @time
       [:div.time (str "Din tid: " (:minutes @time) " minuter " (:seconds @time) " sekunder")])
     [:div [:a {:href (freq-words-2.routes/root-path)} "Tillbaka"]]]))

(defn- group-in-progress
  [words]
  (if-let [word (first words)]
    (show-word word)
    (group-finished)))

(defn- group
  [group-index words]
  (let [current-words (subscribe [:current-words])]
    (fn []
      [:div {:class-name (str "container-game group-" (inc group-index))}
       (if-not @current-words
         [group-intro group-index words]
         [group-in-progress @current-words])])))

(defn app
  []
  (let [selected-group (subscribe [:selected-group])]
    (fn []
      [:div
       (if-not @selected-group
         [group-selection word-groups]
         [group @selected-group (nth word-groups @selected-group)])
       [:footer.footer [:div.container "© Kjell Staffans 2016"]]])))
