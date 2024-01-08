# Bookstore-Management

## Business requirements:
---

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

<details><summary><b>Folder with all the controllers</b></summary>

<img src="pictures/controllers.png" alt="endpoints" style="display: block; margin-left: auto; margin-right: auto;">

</details>

<details><summary><b>Endpoint example to show @RestController annotation, the URL and some CRUD operations</b></summary>

![restcontroller_book](pictures/restcontroller_book.png)

</details>

<details><summary><b>Present all the endpoints in swagger</b></summary>

![swagger_endpoints](pictures/swagger_endpoints.png)

</details>

### Beans for defining services

<details><summary><b>Picture of the folder with all the services</b></summary>

<img src="pictures/services.png" alt="services" style="display: block; margin-left: auto; margin-right: auto;">

</details>

<details><summary><b>Example to show @Service annotation and some business logic implementation</b></summary>

![service_bean](pictures/service_bean.png)

</details>

### Beans for defining repositories. One repository per entity.

<details><summary><b>Folder with all the entities and repositories</b></summary>

<img src="pictures/entities.png" alt="entities" style="margin-top:20px; display: block; margin-left: auto; margin-right: auto;">
<img src="pictures/repositories.png" alt="repositories" style="margin-top:20px; display: block; margin-left: auto; margin-right: auto;">

</details>

<details><summary><b>Example of beans for defining repositories to show @Repository annotation and the link with JPARepository</b></summary>

![repository_book](pictures/repository_book.png)
![repository_bookbasket](pictures/repository_bookbasket.png)

</details>

### Unit tests for all REST endpoints and services

### The data within the application should be persisted in a database. Define at least 6 entities that will be persisted in the database database, and at least 4 relations between them.

<details><summary><b>Picture of the folder with all the entities</b></summary>

<img src="pictures/entities.png" alt="entities" style="margin-top:20px; display: block; margin-left: auto; margin-right: auto;">

</details>

<details><summary><b>Pictures of the database and the diagram to present the entities and the relations</b></summary>

<img src="pictures/database.png" alt="database" style="display: block; margin-left: auto; margin-right: auto;">

![diagram](pictures/MySQLWorkbenchDiagram.png)

</details>

### Validate the POJO classes. You can use the existing validation constraints or create your own annotations if you need a custom constraint

<details><summary><b>Present the @Valid annotation in Book Controller</b></summary>

![validation.png](pictures/validation.png)

</details>

<details><summary><b>Present the validation constraints in Book Entity</b></summary>

![constraints.png](pictures/constraints.png)

</details>

### Document the functionalities in the application such that anyone can use it after reading the document. Every API will be documented by Swagger

The route to access the swagger: [<i>http://localhost:8000/swagger-ui/index.html#/</i>](http://localhost:8000/swagger-ui/index.html#/)

<details><summary><b>Picture of the swagger</b></summary>

![swagger](pictures/swagger.png)

</details>

### The functionality of the application will be demonstrated using Postman







