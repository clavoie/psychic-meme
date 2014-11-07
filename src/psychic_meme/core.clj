(ns psychic-meme.core)

(defn create-dawg
  "Creates an empty word graph, or creates a new one if an existing edge map
   and node map is supplied."
  ([]
     (create-dawg {nil []} {}))
  ([edge-map node-map]
     {:edge-map edge-map
       :node-map node-map}))

(defn edge-to-char?
  "Tests if there is an existing edge to a character from the current node in
   the word graph.

   edges       - sequence of node identifiers for the current node in the graph
   node-map    - the map of all the nodes in the graph
   search-char - the character to test if there is an existing edge from this node"
  [edges node-map search-char]
  (some (fn [node-key]
           (let [node-value (node-map node-key)]
             (if (= node-value search-char) node-key)))
        edges))

(defn dawg-if-missing [edges node-map search-char]
  (let [edge-id (edge-to-char? edges node-map search-char)
        edges (if-not edges [] edges)]
    (if edge-id
      [edges node-map edge-id]
      (let [node-id (gensym)]
        [(conj edges node-id) (assoc node-map node-id search-char) node-id]))))

(defn dawg-add-word [dawg word]
  (let [str-word (str word)
        word-seq (seq str-word)]
    (loop [edge-map (:edge-map dawg)
           node-map (:node-map dawg)
           char-to-add (first word-seq)
           rest-chars (rest word-seq)
           current-node nil]
      (if (nil? char-to-add)
        (create-dawg edge-map node-map)
        (let [[new-edges new-node-map node-id] (dawg-if-missing (edge-map current-node) node-map char-to-add)]
          (recur (assoc edge-map current-node new-edges)
                 new-node-map
                 (first rest-chars)
                 (rest rest-chars)
                 node-id))))))
