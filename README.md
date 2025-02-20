# Demo banking app

- To start the app, simply run it in your IDE or terminal. No additional configuration needed:

```
mvn clean install
java -jar target/dmitriy-shmatkov-test-task-1.0.jar 
```

- To access the database, go to H2 console by URL http://localhost:8080/h2-console or connect to it
  by URL `jdbc:h2:file:./data/banking-db;AUTO_SERVER=TRUE` in your IDE with username `sa` and empty password.
- To view generated test coverage report, open file `/target/site/jacoco/index.html` in your browser.