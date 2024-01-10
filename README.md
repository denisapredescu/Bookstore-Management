# Bookstore Management

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
14. The admin can add and remove the matching between a book and categories/author.

## <i>5 main features:</i>


## <i>REST endpoints for all the features defined for the MVP</i>

<details><summary><b>Folder with all the controllers</b></summary>

<p align="center">
  <img src="pictures/controllers.png" alt="endpoints">
</p>

</details>

<details><summary><b>Endpoint example to show <i>@RestController</i> annotation, the URL and some CRUD operations</b></summary>

<p align="center">
  <img src="pictures/restcontroller_book.png" alt="restcontroller_book">
</p>

</details>

<details><summary><b>Present all the endpoints in swagger</b></summary>

<p align="center">
  <img src="pictures/swagger_endpoints.png" alt="swagger_endpoints">
</p>

</details>

## <i>Beans for defining services</i>

<details><summary><b>Picture of the folder with all the services</b></summary>

<p align="center">
  <img src="pictures/services.png" alt="services">
</p>

</details>

<details><summary><b>Example to show <i>@Service</i> annotation and some business logic implementation</b></summary>

<p align="center">
  <img src="pictures/service_bean.png" alt="service_bean">
</p>

</details>

## <i>Beans for defining repositories. One repository per entity</i>

<details><summary><b>Folder with all the entities and repositories</b></summary>
<p align="center">
  <img src="pictures/entities.png" alt="entities">
  <br>
  <img src="pictures/repositories.png" alt="repositories" style="margin-top:20px;">
</p>
</details>

<details>
  <summary><b>Example of beans for defining repositories to show <i>@Repository</i> annotation and the link with JPARepository</b></summary>

  <p align="center">
    <img src="pictures/repository_book.png" alt="repository_book">
    <br>
    <img src="pictures/repository_bookbasket.png" alt="repository_bookbasket" style="margin-top:20px;">
  </p>

</details>

## <i>Unit tests for all REST endpoints and services</i>


## <i>The data within the application should be persisted in a database. Define at least 6 entities that will be persisted in the database, and at least 4 relations between them</i>
 

<details><summary><b>Picture of the folder with all the entities</b></summary>

<p align="center">
  <img src="pictures/entities.png" alt="entities">
</p>

</details>

<details><summary><b>Pictures of the database and the diagram to present the entities and the relations</b></summary>

<p align="center">
  <img src="pictures/database.png" alt="database">
</p>

<p align="center">
  <img src="pictures/MySQLWorkbenchDiagram.png" alt="diagram">
</p>

</details>

## <i>Validate the POJO classes. You can use the existing validation constraints or create your own annotations if you need a custom constraint</i>

<details><summary><b>Present the <i>@Valid</i> annotation in Book Controller</b></summary>

<p align="center">
  <img src="pictures/validation.png" alt="validation">
</p>

</details>

<details><summary><b>Present the validation constraints in Book Entity</b></summary>

<p align="center">
  <img src="pictures/constraints.png" alt="constraints">
</p>

</details>

## <i>Document the functionalities in the application such that anyone can use it after reading the document. Every API will be documented by Swagger</i>
 
The route to access the swagger: [<i>http://localhost:8000/swagger-ui/index.html#/</i>](http://localhost:8000/swagger-ui/index.html#/)

<details><summary><b>Picture of the swagger</b></summary>

<p align="center">
  <img src="pictures/swagger.png" alt="swagger">
</p>

</details>

## <i>The functionality of the application will be demonstrated using Postman</i>

To add a parameter in the request header using *@RequestHeader* you should write 
> **pm.request.headers.add("foo: bar");**
> 
> where:<br>
> **foo** is the key <br>
> **bar** is the value

<p align="center">
  <img src="pictures/request header example.png" alt="request header example">
</p>

<details><summary><b>The collection of functionalities of the application in Postman</b></summary>

<p align="center">
  <img src="pictures/functions in postman.png" alt="functions in postman">
</p>

</details>

<details><summary><b>Example of functionality of the application called in Postman</b></summary>

<p align="center">
  <img src="pictures/getBasket.png" alt="getBasket functionality">
</p>

</details>

## <i>Exceptions</i>

<details><summary><b>Folder with the exceptions</b></summary>

<p align="center">
  <img src="pictures/exception_folder.png" alt="exception_folder">
</p>

</details>

### Example of thrown and caught exceptions 
<details><summary><b>EmailAlreadyUsedException</b></summary>

<p align="center">
  <img src="pictures/EmailAlreadyUsedException.png" alt="EmailAlreadyUsedException">
</p>

</details>


<details><summary><b>DeletedBookException</b></summary>

<p align="center">
  <img src="pictures/DeletedBookException.png" alt="DeletedBookException">
</p>

</details>


<details><summary><b>NoSuchElementException</b></summary>

<p align="center">
  <img src="pictures/NoSuchElementException.png" alt="NoSuchElementException">
    <img src="pictures/NoSuchElementException 1.png" alt="NoSuchElementException 1">
</p>

</details>

<details><summary><b>UserNotLoggedInException</b></summary>

<p align="center">
  <img src="pictures/UserNotLoggedInException.png" alt="UserNotLoggedInException">
</p>

</details>

<details><summary><b>UnauthorizedUserException</b></summary>

<p align="center">
  <img src="pictures/UnauthorizedUserException.png" alt="UnauthorizedUserException">
</p>

</details>

<details><summary><b>InvalidTokenException</b></summary>

<p align="center">
  <img src="pictures/InvalidTokenException.png" alt="InvalidTokenException">
</p>

</details>