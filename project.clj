(defproject psychic-meme "1.0.1-SNAPSHOT"
  :description "Radix tree for Clojure"
  :url "https://github.com/clavoie/psychic-meme"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :global-vars {*warn-on-reflection* true}
  :plugins [[codox "0.6.6"]]
  :codox {:src-dir-uri "http://github.com/clavoie/psychic-meme/blob/master/"
          :src-linenum-anchor-prefix "L"}
  :test-selectors {:default (complement :single)
                   :single :single
                   :all (fn [_] true)})
