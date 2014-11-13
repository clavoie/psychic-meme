(ns psychic-meme.private.dawg)

(defn create-node [value]
  {:edges #{} :value value})

(defn get-edges [dawg node-id]
  (get-in dawg [node-id :edges]))

(defn get-value [dawg node-id]
  (get-in dawg [node-id :value]))

(defn add-edge [dawg from-node-id to-node-id]
  (update-in dawg [from-node-id :edges] #(conj % to-node-id)))

(defn search-fn [dawg value]
  #(if (= (get-value dawg %) value) %))

(defn child? [dawg node-id value]
  (some (search-fn dawg value)
        (get-edges dawg node-id)))

(defn add-child [dawg node-id value]
  (if-let [child-id (child? dawg node-id value)]
    [dawg child-id]
    (let [child-id (gensym)
          dawg (assoc dawg child-id (create-node value))
          dawg (add-edge node-id child-id)]
      [dawg child-id])))
