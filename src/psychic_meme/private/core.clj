(ns psychic-meme.private.core)

(defn get-graph
  "Returns the graph of nodes which makes up the radix tree

  radix-tree - the radix tree"
  [radix-tree]
  (get radix-tree :graph))

(defn get-equality-fn
  "Returns the equality fn used to compare nodes of the radix tree

  radix-tree - the radix tree"
  [radix-tree]
  (get radix-tree :equality-fn))

(defn create-node
  "Creates a new node in the directed graph.

  value - the value of the node"
  [value]
  {:edges #{} :value value})

(defn add-node
  "Adds a new node to the graph, returning the updated graph

  radix-tree - the radix tree
  node-id - the id of the node to add to the graph
  value - the value of the node"
  [radix-tree node-id value]
  (assoc-in radix-tree [:graph node-id] (create-node value)))

(defn get-edges
  "Returns the edges for a node

  radix-tree - the radix tree containing the node
  node-id - the id of the node in the graph"
  [radix-tree node-id]
  (get-in radix-tree [:graph node-id :edges]))

(defn get-value
  "Returns the value for a node

  radix-tree - the radix tree containing the node
  node-id - the id of the node in the graph"
  [radix-tree node-id]
  (get-in radix-tree [:graph node-id :value]))

(defn add-edge
  "Adds an edge from a parent node to a child node

  radix-tree - the radix tree containing the nodes
  from-id - the node id from which the edge will be originating
  to-id - the node id to which the edge terminates"
  [radix-tree from-id to-id]
  (update-in radix-tree [:graph from-id :edges] #(conj % to-id)))

(defn search-fn
  "Returns a function which searches for a node with a particular value.

  Returns a fn that takes an edge node-id as a parameter,
  and returns the first edge node-id of the edge node which has
  value as its value.

  radix-tree - the radix tree containing the nodes to search
  value - the value to search for"
  [radix-tree value]
  #(if ((get-equality-fn radix-tree) (get-value radix-tree %) value) %))

(defn get-child
  "Returns the edge id of the edge node with a specific value

  radix-tree - the radix tree containing the nodes
  node-id - the node with the edges that should be searched
  value - the value to search for"
  [radix-tree node-id value]
  (some (search-fn radix-tree value)
        (get-edges radix-tree node-id)))

(defn add-child
  "Adds a child node to a node with a particular value, if it does not already exist.
  Returns a vector where the first item is the new graph, and the second item is the
  child node's id

  radix-tree - the radix tree containing the nodes
  node-id - the node id to add the child to
  value - the value to add to the node"
  [radix-tree node-id value]
  (if-let [child-id (get-child radix-tree node-id value)]
    [radix-tree child-id]
    (let [child-id (gensym)
          radix-tree (add-node radix-tree child-id value)
          radix-tree (add-edge radix-tree node-id child-id)]
      [radix-tree child-id])))


(defn add-value
  "Adds a single value to the graph, returning the new graph

  radix-tree - the radix tree to which the value will be added
  value - the value to add to the graph. (seq value) will be used to
          generate the seq values added to the graph"
  [radix-tree value]
  (let [seq-value (seq value)]
    (loop [node-id nil
           value (first seq-value)
           other-values (rest seq-value)
           radix-tree radix-tree]
      (if (nil? value)
        (add-edge radix-tree node-id :eos)
        (let [[radix-tree child-id] (add-child radix-tree node-id value)]
          (recur child-id
                 (first other-values)
                 (rest other-values)
                 radix-tree))))))

(defn get-final-node
  "Returns the final node-id in the sequence (seq value), or nil if the given
  sequence does not exist in the graph

  radix-tree - the radix tree to search
  value - the value to use to search the graph. (seq value) will be used
          to search the graph"
  [radix-tree value]
  (if-let [seq-value (seq value)]
    (loop [node-id nil
           value (first seq-value)
           other-values (rest seq-value)]
      (if (nil? value)
        node-id
        (if-let [child-id (get-child radix-tree node-id value)]
          (recur child-id
                 (first other-values)
                 (rest other-values)))))))
