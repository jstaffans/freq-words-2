(ns freq-words-2.macros)

(defmacro fore
  "Eager for"
  [seq-exprs body-expr]
  `(doall
     (for ~seq-exprs ~body-expr)))
