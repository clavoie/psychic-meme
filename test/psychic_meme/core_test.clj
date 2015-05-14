(ns psychic-meme.core-test
  (:require [clojure.test :refer :all]
            [psychic-meme.core :refer :all]
            [psychic-meme.private.core :as private]))

(deftest create-test
  (let [radix-tree (create)]
    (is (map? radix-tree))
    (is (fn? (private/get-equality-fn radix-tree)))
    (is (sorted? (private/get-graph radix-tree)))
    (is (not (nil? (get-in radix-tree [:graph nil]))))))

(deftest add-custom-objects-test
  (let [my-equality-fn #(= (get %1 :a) (get %2 :a))
        radix-tree (create :equality-fn my-equality-fn)
        radix-tree (add radix-tree [[{:a 1} {:a 2} {:a 3}] [{:a 1} {:a 2} {:a 4}]])
        node-count (count (private/get-graph radix-tree))
        root-edges (count (private/get-edges radix-tree nil))]
    (is (= node-count 5))
    (is (= root-edges 1))))

(deftest add-test
  (let [graph (create)
        graph (add graph ["hello" "hi"])
        node-count (count (private/get-graph graph))
        root-edges (count (private/get-edges graph nil))]
    (is (= node-count 7))
    (is (= root-edges 1))))

(deftest items-test
  (let [graph (create)
        words ["hello" "hit" "apple"]
        graph (add graph words)
        items (items graph)
        str-items (for [item items] (apply str item))]
    (doseq [word words]
      (is (some #(= word %) str-items)))))

(deftest complete-test
  (let [graph (create)
        words ["hello" "hit" "apple" "help"]
        graph (add graph words)
        completions (complete graph "he")
        str-items (set (for [item completions] (apply str item)))]
    (is (nil? (complete graph "")))
    (is (nil? (complete graph "z")))
    (is (contains? str-items "hello"))
    (is (contains? str-items "help"))))
