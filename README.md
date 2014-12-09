# psychic-meme

[Radix Tree](http://en.wikipedia.org/wiki/Radix_tree) for Clojure

## Usage

```clojure
user=> (require ['psychic-meme.core :refer ['create 'add 'items 'complete]])
nil

user=> (def graph (create))
#'user/graph

user=> (def graph (add graph ["hello" "hi" "able" "help"]))
#'user/graph

user=> (items graph)
((\a \b \l \e) (\h \e \l \l \o) (\h \e \l \p) (\h \i))

user=> (for [item (items graph)] (apply str item))
("able" "hello" "help" "hi")

user=> (complete graph "he")
((\h \e \l \l \o) (\h \e \l \p))

user=> (for [item (complete graph "he")] (apply str item))
("hello" "help")

;; can be used for anything that is a sequence
user=> (def graph2 (create))
#'user/graph2

user=> (def graph2 (add graph2 [[1 2 3] [1 4 5] [2 6 7] [1 2 7]]))
#'user/graph2

user=> (items graph2)
((1 2 7) (1 2 3) (1 4 5) (2 6 7))

user=> (complete graph2 [1])
((1 2 7) (1 2 3) (1 4 5))

user=> (complete graph2 [1 2])
((1 2 7) (1 2 3))

```
## Todos

1. Allow the user to define an equality function for comparing node values
2. Allow the graph to be initialized with a collection. Currently `(create)` => `(add)` seems too clunky
3. Allow updating of items in the sequence

### Why is it called psychic-meme?

Couldn't think of a good name for this project, and GitHub's auto-generated name seemed like it was a pretty close fit.

## License

Copyright © 2014 Chris LaVoie

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
