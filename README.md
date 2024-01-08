# Bookstore-Management

![diagram](pictures/MySQLWorkbenchDiagram.png)

## <i>Business requirements:</i>

1. The admin can add, update and hard delete authors.
2. The user can get all the authors with their details from database.
3. The admin can add, update, hard delete categories.
4. The user can get all the categories.
5. The admin can add, update, soft delete and get all the books.
6. The user can see all the available books.
7. The user can filter books by author or by category.
8. The user can see his basket.
9. The user can add and remove books from his basket.
10. The user can increase and decrease numbers of copies of books from his basket.
11. The user can submit order.
12. The user can make an account, login and soft delete his account.
13. The admin can see a list of all users.


## <i>5 main features:</i>


### REST endpoints for all the features defined for the MVP

<img src="pictures/controllers.png" alt="endpoints" style="display: block; margin-left: auto; margin-right: auto;">

![swagger_endpoints](pictures/swagger_endpoints.png)

### Beans for defining services

### Beans for defining repositories One repository per entity.

### Document the functionalities in the application such that anyone can use it after reading the document. Every API will be documented by Swagger

The route to access the swagger: [<i>http://localhost:8000/swagger-ui/index.html#/</i>](http://localhost:8000/swagger-ui/index.html#/)

![swagger](pictures/swagger.png)
