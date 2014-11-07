(ns psychic-meme.core-test
  (:require [clojure.test :refer :all]
            [psychic-meme.core :refer :all]))

(deftest create-dawg-test
  (testing "Can create an empty word graph"
    (is (map? (create-dawg))))
  (testing "Car create a word graph using existing data"
    (let [my-edge-map {nil [] 'a ['b]}
          my-node-map {'a 1 'b 2}
          dawg (create-dawg my-edge-map my-node-map)
          {:keys [edge-map node-map]} dawg]
      (is (= edge-map my-edge-map))
      (is (= node-map my-node-map)))))
