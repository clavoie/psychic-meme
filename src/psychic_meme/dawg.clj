(ns psychic-meme.dawg)

(defn create
  "Creates a new directed acyclic word graph"
  []
  (sorted-map nil (create-node nil)))

(defn add
  "Adds the items of a seq to the graph"
  [dawg value]
  (let [seq-value (seq value)]
    (loop [node-id nil
           value (first seq-value)
           other-values (rest seq-value)
           dawg dawg]
      (if (nil? value)
        (add-edge dawg node-id :eos)
        (let [[dawg child-id] (add-child dawg node-id value)]
          (recur child-id
                 (first other-values)
                 (rest other-values)
                 dawg))))))
