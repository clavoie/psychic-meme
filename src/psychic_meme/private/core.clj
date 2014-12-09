(ns psychic-meme.private.core)

(defn create-node
  "Creates a new node in the directed graph.

  value - the value of the node"
  [value]
  {:edges #{} :value value})

(defn add-node
  "Adds a new node to the graph, returning the updated graph

  graph - the graph
  node-id - the id of the node to add to the graph
  value - the value of the node"
  [graph node-id value]
  (assoc graph node-id (create-node value)))

(defn get-edges
  "Returns the edges for a node

  graph - the graph containing the node
  node-id - the id of the node in the graph"
  [graph node-id]
  (get-in graph [node-id :edges]))

(defn get-value
  "Returns the value for a node

  graph - the graph containing the node
  node-id - the id of the node in the graph"
  [graph node-id]
  (get-in graph [node-id :value]))

(defn add-edge
  "Adds an edge from a parent node to a child node

  graph - the graph containing the nodes
  from-id - the node id from which the edge will be originating
  to-id - the node id to which the edge terminates"
  [graph from-id to-id]
  (update-in graph [from-id :edges] #(conj % to-id)))

(defn search-fn
  "Searches for a node with a particular value.

  Returns a fn that takes an edge node-id as a parameter,
  and returns the first edge node-id of the edge node which has
  value as its value.

  graph - the graph containing the nodes to search
  value - the value to search for"
  [graph value]
  #(if (= (get-value graph %) value) %))

(defn get-child
  "Returns the edge id of the edge node with a specific value

  graph - the graph containing the nodes
  node-id - the node with the edges that should be searched
  value - the value to search for"
  [graph node-id value]
  (some (search-fn graph value)
        (get-edges graph node-id)))

(defn add-child
  "Adds a child node to a node with a particular value, if it does not already exist.
  Returns a vector where the first item is the new graph, and the second item is the
  child node's id

  graph - the graph containing the nodes
  node-id - the node id to add the child to
  value - the value to add to the node"
  [graph node-id value]
  (if-let [child-id (get-child graph node-id value)]
    [graph child-id]
    (let [child-id (gensym)
          graph (add-node graph child-id value)
          graph (add-edge graph node-id child-id)]
      [graph child-id])))


(defn add-value
  "Adds a single value to the graph, returning the new graph

  graph - the graph to which the value will be added
  value - the value to add to the graph. (seq value) will be used to
          generate the seq values added to the graph"
  [graph value]
  (let [seq-value (seq value)]
    (loop [node-id nil
           value (first seq-value)
           other-values (rest seq-value)
           graph graph]
      (if (nil? value)
        (add-edge graph node-id :eos)
        (let [[graph child-id] (add-child graph node-id value)]
          (recur child-id
                 (first other-values)
                 (rest other-values)
                 graph))))))

(defn get-final-node
  "Returns the final node-id in the sequence (seq value), or nil if the given
  sequence does not exist in the graph

  graph - the graph to search
  value - the value to use to search the graph. (seq value) will be used
          to search the graph"
  [graph value]
  (if-let [seq-value (seq value)]
    (loop [node-id nil
           value (first seq-value)
           other-values (rest seq-value)]
      (if (nil? value)
        node-id
        (if-let [child-id (get-child graph node-id value)]
          (recur child-id
                 (first other-values)
                 (rest other-values)))))))
