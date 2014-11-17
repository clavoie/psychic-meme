(ns psychic-meme.dawg
  (:require [psychic-meme.private.dawg :as private]))

(defn create
  "Creates a new directed acyclic graph"
  []
  (sorted-map nil (private/create-node nil)))

(defn add
  "Adds the items of a seq to the graph

  dawg - the graph to add the items to
  value - the value to add to the graph. (seq value) will be used to create the
          sequence which is added to the graph"
  [dawg value]
  (let [seq-value (seq value)]
    (loop [node-id nil
           value (first seq-value)
           other-values (rest seq-value)
           dawg dawg]
      (if (nil? value)
        (private/add-edge dawg node-id :eos)
        (let [[dawg child-id] (private/add-child dawg node-id value)]
          (recur child-id
                 (first other-values)
                 (rest other-values)
                 dawg))))))

(defn items
  "Returns a lazy sequence of all the items in the graph
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
