(ns psychic-meme.dawg-test
  (:require [clojure.test :refer :all]
            [psychic-meme.dawg :refer :all]
            [psychic-meme.private.dawg :as private]))

(deftest create-test
  (testing "Creating graph"
    (let [graph (create)]
      (is (map? graph))
      (is (sorted? graph))
      (is (not (nil? (get graph nil)))))))

(deftest add-test
  (testing "Adding an item to the graph"
    (let [graph (create)
          graph (add (add graph "hello") "hi")
          node-count (count graph)
          root-edges (count (private/get-edges graph nil))]
      (is (= node-count 7))
      (is (= root-edges 1)))))

(deftest items-test
  (let [graph (create)
        graph (add (add (add graph "hello") "hit") "apple")
        items (items graph)
        str-items (for [item items] (apply str item))]
    (println str-items)
    (is (= 1 1))))
