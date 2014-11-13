(ns psychic-meme.private.dawg-test
  (:require [clojure.test :refer :all]
            [psychic-meme.dawg :as dawg]
            [psychic-meme.private.dawg :refer :all]))

(deftest create-node-test
  (testing "Node created as a map with appropriate fields"
    (let [test-value 1
          node (create-node test-value)
          {:keys [edges value]} node]
      (is (map? node))
      (is (set? edges))
      (is (empty? edges))
      (is (= test-value value)))))

(deftest add-node-test
  (testing "Node added to graph"
    (let [node-id (gensym)
          node-value 1
          graph (dawg/create)
          graph (add-node graph node-id node-value)]
      (is (= node-value (get-value graph node-id))))))

(deftest get-edges-test
  (testing "Default edges returned"
    (let [graph (dawg/create)
          edges (get-edges graph nil)]
      (is (set? edges))
      (is (empty? edges))))
  (testing "Contains the correct edge values"
    (let [child-id (gensym)
          graph (add-edge (dawg/create) nil child-id)
          edges (get-edges graph nil)]
      (is (= #{child-id} edges)))))

(deftest get-value-test
  (testing "Default node value returned"
    (let [graph (dawg/create)
          value (get-value graph nil)]
      (is (nil? value))))
  (testing "Set node value is returned"
    (let [test-value 1
          graph (update-in (dawg/create) [nil :value] (fn [v] test-value))
          value (get-value graph nil)]
      (is (= value test-value)))))

(deftest add-edge-test
  (testing "Edge added to the graph"
    (let [to-id (gensym)
          graph (add-edge (dawg/create) nil to-id)
          edge (first (get-edges graph nil))]
      (is (= to-id edge)))))

(deftest search-fn-test
  (testing "Creates a search fn for the graph"
    (let [value-1 1
          value-2 2
          id-1 (gensym)
          id-2 (gensym)
          node-1 (create-node value-1)
          node-2 (create-node value-2)
          graph (assoc (dawg/create) id-1 node-1 id-2 node-2)
          graph (add-edge (add-edge graph nil id-1) nil id-2)
          search-fn-1 (search-fn graph value-1)
          search-fn-2 (search-fn graph value-2)]
      (is (= id-1 (some search-fn-1 (get-edges graph nil))))
      (is (= id-2 (some search-fn-2 (get-edges graph nil)))))))

(deftest get-child-test
  (testing "Child not found in edges"
    (let [graph (dawg/create)]
      (is (nil? (get-child graph nil 1)))))
  (testing "Child found in edges"
    (let [value-1 1
          value-2 2
          id-1 (gensym)
          id-2 (gensym)
          node-1 (create-node value-1)
          node-2 (create-node value-2)
          graph (assoc (dawg/create) id-1 node-1 id-2 node-2)
          graph (add-edge (add-edge graph nil id-1) nil id-2)]
      (is (= id-1 (get-child graph nil value-1)))
      (is (= id-2 (get-child graph nil value-2))))))

(deftest add-child-test
  (testing "New child added to graph"
    (let [node-value 1
          graph (dawg/create)
          [graph node-id] (add-child graph nil node-value)]
      (is (= node-value (get-value graph node-id)))
      (is (= node-id (first (get-edges graph nil))))))
  (testing "Existing child added to graph"
    (let [node-value 1
          graph (dawg/create)
          [graph node-id-1] (add-child graph nil node-value)
          [graph node-id-2] (add-child graph nil node-value)]
      (is (= node-value (get-value graph node-id-1)))
      (is (= node-id-1 node-id-2)))))
