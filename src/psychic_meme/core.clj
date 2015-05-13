(ns psychic-meme.core
  (:require [psychic-meme.private.core :as private]))

(defn create
  "Creates a new directed acyclic graph

  keys
    :equality-fn - a function used to compare elements of the sequence.
                   If no function is supplied  clojure.core/= is used by default"
  [& {:keys [equality-fn] :or {equality-fn clojure.core/=}}]
  {:graph (sorted-map nil (private/create-node nil))
   :equality-fn equality-fn})

(defn add
  "Adds the items of a seq to the graph, returning the new graph.

  graph - the graph to add the items to
  values - the values to add to the graph. Each value is a sequence.
           (seq value) will be used to create the sequence which is added to the graph"
  [graph values]
  (loop [graph graph
         values values]
    (if (empty? values)
      graph
      (recur
       (private/add-value graph (first values))
       (rest values)))))

(defn items
  "Returns a lazy sequence of all the reconstructed sequences in the graph,
  or an empty sequence if the graph contains no sequences. Example:

  ((1 2 3) (1 4 5) (1 6 7))

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

(defn complete
  "Given a sequence returns all the completed sequences. nil is returned
  if no sequence completions can be found.

  graph - the graph to search for completions
  value - the value to search the graph for. value is expected to be
          convertable into a sequence."
  [graph value]
  (if-let [final-node (private/get-final-node graph value)]
    (for [item (items graph final-node)]
      (concat (seq value) (rest item)))))
