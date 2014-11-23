(ns psychic-meme.private.dawg)

(defn create-node
  "Creates a new node in the directed graph.

  value - the value of the node"
  [value]
  {:edges #{} :value value})

(defn add-node
  "Adds a new node to the graph, returning the updated graph

  dawg - the graph
  node-id - the id of the node to add to the graph
  value - the value of the node"
  [dawg node-id value]
  (assoc dawg node-id (create-node value)))

(defn get-edges
  "Returns the edges for a node

  dawg - the graph containing the node
  node-id - the id of the node in the graph"
  [dawg node-id]
  (get-in dawg [node-id :edges]))

(defn get-value
  "Returns the value for a node

  dawg - the graph containing the node
  node-id - the id of the node in the graph"
  [dawg node-id]
  (get-in dawg [node-id :value]))

(defn add-edge
  "Adds an edge from a parent node to a child node

  dawg - the graph containing the nodes
  from-id - the node id from which the edge will be originating
  to-id - the node id to which the edge terminates"
  [dawg from-id to-id]
  (update-in dawg [from-id :edges] #(conj % to-id)))

(defn search-fn
  "Searches for a node with a particular value.

  Returns a fn that takes an edge node-id as a parameter,
  and returns the first edge node-id of the edge node which has
  value as its value.

  dawg - the graph containing the nodes to search
  value - the value to search for"
  [dawg value]
  #(if (= (get-value dawg %) value) %))

(defn get-child
  "Returns the edge id of the edge node with a specific value

  dawg - the graph containing the nodes
  node-id - the node with the edges that should be searched
  value - the value to search for"
  [dawg node-id value]
  (some (search-fn dawg value)
        (get-edges dawg node-id)))

(defn add-child
  "Adds a child node to a node with a particular value, if it does not already exist.
  Returns a vector where the first item is the new graph, and the second item is the
  child node's id

  dawg - the graph containing the nodes
  node-id - the node id to add the child to
  value - the value to add to the node"
  [dawg node-id value]
  (if-let [child-id (get-child dawg node-id value)]
    [dawg child-id]
    (let [child-id (gensym)
          dawg (add-node dawg child-id value)
          dawg (add-edge dawg node-id child-id)]
      [dawg child-id])))


(defn add-value
  "Adds a single value to the graph, returning the new graph

  dawg - the graph to which the value will be added
  value - the value to add to the graph. (seq value) will be used to
          generate the seq values added to the graph"
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
