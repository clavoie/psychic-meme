(ns psychic-meme.private.dawg-test
  (:require [clojure.test :refer :all]
            [psychic-meme.private.dawg :refer :all]))

(deftest create-node-test
  (testing "Node created as a map with appropriate fields"
    (let [test-value 1
          node (create-node test-value)
          {:keys [edges value]} node]
      (is (map? node))
      (is (set? edges))
      (is (zero? (count edges)))
      (is (= test-value value)))))
