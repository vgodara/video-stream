This is implementation of Clean architecture using gradle  multi module features.
Currently this has users service which contains following layers
1. Usecase: This layer has business logic only.
2. Datastore: This layer provideds access to stored value in database or cached value.
3. Database: This layer has actual implement to fetch data from MySQL server.
4. Controller: This layer acts as intermediary between actual http request and usecase layer.
5. Application: This layer glues everything together and runs smooth application.

Benifit of this approach is that we can have entire functioning app written without using any specific framework and the framework can be extracted away in database and application layer 
