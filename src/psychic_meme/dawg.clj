(ns psychic-meme.dawg
  (:require [psychic-meme.private.dawg :as private]))

(defn create
  "Creates a new directed acyclic word graph"
  []
  (sorted-map nil (private/create-node nil)))

(defn add
  "Adds the items of a seq to the graph"
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
