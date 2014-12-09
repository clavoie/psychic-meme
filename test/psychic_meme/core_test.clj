(ns psychic-meme.core-test
  (:require [clojure.test :refer :all]
            [psychic-meme.core :refer :all]
            [psychic-meme.private.core :as private]))

(deftest create-test
  (let [graph (create)]
    (is (map? graph))
    (is (sorted? graph))
    (is (not (nil? (get graph nil))))))

(deftest add-test
  (let [graph (create)
        graph (add graph ["hello" "hi"])
        node-count (count graph)
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
