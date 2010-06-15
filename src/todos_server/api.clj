(ns todos-server.api
  (:use clojure.contrib.json.write
        clojure.contrib.json.read
        clojure.contrib.duck-streams
        compojure.core
        todos-server.db))

(defn- emit-json
  "Turn the object to JSON, and emit it with the correct content type"
  [x]
  {:headers {"Content-Type" "application/json"}
   :body    (json-str {:content x})})

(defn- parse-json
  "Parse the request body into a Clojure data structure"
  [body]
  (read-json (slurp* body)))

(defn guid [task]
  (str "/tasks/" (:id task)))

(defn with-guid [task]
  (assoc task :guid (guid task)))

(defroutes main-routes
  (GET    "/tasks" []
    (emit-json
      (map with-guid (find-all-tasks))))
  (GET    "/tasks/:id" [id]
    (emit-json
      (with-guid (find-task id))))
  (POST   "/tasks" {body :body}
    (let [saved-task (add-task (parse-json body))]
      {:status 201
       :headers {"Location" (guid saved-task)}}))
  (PUT    "/tasks/:id" {body :body {id "id"} :route-params}
    (update-task id (parse-json body)))
  (DELETE "/tasks/:id" [id]
    (destroy-task id)
    {:status 200}))


  