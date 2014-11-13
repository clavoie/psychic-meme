(ns psychic-meme.search)

;; not done yet
(defn complete [dawg word]
  (let [str-word (str-word)
        word-seq (seq str-word)
        edge-map (:edge-map dawg)
        node-map (:node-map dawg)]
    (loop )))

(defn words-for-node [node-id dawg path]
  (let [{:keys [edge-map node-map]} dawg
        path (str path (node-map node-id))]
    ;; foreach edge, if EOW return path, else get the words for that node
    (map #(str path (node-map %))
         (edge-map node-id))))
