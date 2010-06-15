(ns todos-server.db
 (:use todos-server.util)
 (:use somnium.congomongo))

(mongo! :db "todos")

(defn- extern-id
  "Returns a version of obj with a string :id based on its :_id"
  [obj]
  (-> obj
      (assoc :id (str (:_id obj)))
      (dissoc :_id)))

(defn- intern-id
  "Returns a version of obj with an ObjectId based on its :id"
  [obj]
  (-> obj
      (assoc :_id (object-id (:id obj)))
      (dissoc :id)))

(defn find-all-tasks []
  (map extern-id (fetch :tasks)))

(defn find-task [id]
  (extern-id (fetch-by-id :tasks id)))

(defn add-task [task]
  (extern-id (insert! :tasks task)))

(defn update-task [id task]
  (let [task-in-db (fetch-by-id :tasks id)]
    (update! :tasks
      task-in-db
      (merge-with-kw-keys task-in-db task))))

(defn destroy-task [id]
  (destroy! :tasks
    (fetch-by-id :tasks id)))
