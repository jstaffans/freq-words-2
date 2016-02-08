(defproject freq-words-2 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [reagent "0.5.1" :exclusions [cljsjs/react]]
                 [cljsjs/react-with-addons "0.13.3-0"]
                 [re-frame "0.7.0-alpha-2"]
                 [secretary "1.2.3"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "scripts"]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-6"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :profiles {:dev {:dependencies [[figwheel "0.5.0-6"]
                                  [figwheel-sidecar "0.5.0-6"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [org.clojure/tools.nrepl "0.2.12"]]
                   :source-paths ["src/cljs"]}}

  :figwheel {:css-dirs ["resources/public/css"]}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs" "dev/cljs"]
                        :figwheel true
                        :compiler {:main freq-words-2.dev
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {:id "min"
                        :source-paths ["src/cljs"]
                        :compiler {:main freq-words-2.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :pretty-print false}}]})
