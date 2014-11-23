(ns psychic-meme.dawg
  (:require [psychic-meme.private.dawg :as private]))

(defn create
  "Creates a new directed acyclic graph"
  []
  (sorted-map nil (private/create-node nil)))

(defn add
  "Adds the items of a seq to the graph, returning the new graph.

  dawg - the graph to add the items to
  values - the values to add to the graph. Each value is a sequence.
           (seq value) will be used to create the sequence which is added to the graph"
  [dawg values]
  (loop [dawg dawg
         values values]
    (if (empty? values)
      dawg
      (recur
       (private/add-value dawg (first values))
       (rest values)))))

(defn items
  "Returns a lazy sequence of all the reconstructed sequences in the graph,
  or an empty sequence if the graph contains no sequences. Example:

  ((1 2 3) (4 5 6) (7 8 9))

  graph - the graph
  node-id - (optional), the id of the node to start reconstructing sequences at in the graph.
            If no id is provided sequences will start being reconstructed from the root node
  "
  ([graph]
   (items graph nil))
  ([graph node-id]
   (cond
    (nil? node-id) (apply concat (for [edge-id (private/get-edges graph node-id)] (items graph edge-id)))
    (= :eos node-id) '()
    true
    (apply concat
           (for [edge-id (private/get-edges graph node-id)
                 :let [node-value (private/get-value graph node-id)]]
             (if (= :eos edge-id)
               ;; not a seq yet, wrap in seq. the extra list will be removed by the apply / concat
               (list (list node-value))
               (map #(cons node-value %) (items graph edge-id))))))))
