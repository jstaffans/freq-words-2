 (ns ^:figwheel-no-load freq-words-2.dev
   (:require [figwheel.client :as figwheel :include-macros true]
             [devtools.core :as devtools]
             [freq-words-2.core :refer [mount-root]]))

(figwheel/watch-and-reload
   :websocket-url "ws://localhost:3449/figwheel-ws"
   :jsload-callback mount-root)

(devtools/enable-feature! :sanity-hints :dirac)
(devtools/install!)
