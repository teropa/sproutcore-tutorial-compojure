(ns todos-server.util)

(defn keywordify-keys
  "Returns a map otherwise same as the argument but
   with all keys turned to keywords"
  [m]
  (zipmap
    (map keyword (keys m))
    (vals m)))

(defn merge-with-kw-keys
  "Merges maps converting all keys to keywords"
  [& maps]
  (reduce
    merge
    (map keywordify-keys maps)))


