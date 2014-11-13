(ns psychic-meme.dawg)

(defn create
  "Creates a new directed acyclic word graph"
  []
  (sorted-map nil {:edges #{} :value nil}))

(defn edges [dawg node-id]
  (get-in dawg [node-id :edges]))

(defn- add-edge [dawg from-node-id to-node-id]
  (update-in dawg [from-node-id :edges] #(conj % to-node-id)))

(defn search-fn [dawg value]
  (fn [node-id]
          (let [v (get-in dawg [node-id :value])]
            (if (= v value) node-id))))

(defn child? [dawg node-id value]
  (some (search-fn dawg value)
        (get-in dawg [node-id :edges])))

(defn- add-child [dawg node-id value]
  )

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
        (recur 'new-node-id
               (first other-values)
               (rest other-values)
               dawg)))))
