(ns psychic-meme.core-test
  (:require [clojure.test :refer :all]
            [psychic-meme.core :refer :all]
            [psychic-meme.private.core :as private]))

(deftest create-test
  (testing "Creating graph"
    (let [graph (create)]
      (is (map? graph))
      (is (sorted? graph))
      (is (not (nil? (get graph nil)))))))

(deftest add-test
  (testing "Adding an item to the graph"
    (let [graph (create)
          graph (add graph ["hello" "hi"])
          node-count (count graph)
          root-edges (count (private/get-edges graph nil))]
      (is (= node-count 7))
      (is (= root-edges 1)))))

(deftest items-test
  (testing "Sequences reconstructed from the graph"
    (let [graph (create)
          words ["hello" "hit" "apple"]
          graph (add graph words)
          items (items graph)
          str-items (for [item items] (apply str item))]
      (doseq [word words]
        (is (some #(= word %) str-items))))))
