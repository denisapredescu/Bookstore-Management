# Bookstore Management

## <i>Business requirements:</i>

1. The admin can add, update and hard delete authors and categories.
2. The user can get all the authors with their details from database or select just one based on his name. 
3. The user can get all the categories. 
4. The admin can add, update, soft delete, link authors and categories to books and get all the books. 
5. The user can see all the available books. 
6. The user can filter books by author or by category. 
7. The user can see his basket and submit an order. 
8. The user can add and remove books, increase and decrease numbers of copies of books from his basket. 
9. The user can make an account, modify non-vital fields from it, login and soft delete his account. 
10. The admin can see a list of all users. 

## <i>5 main features:</i>

<details><summary><b>Link an author to a book feature</b></summary>

The link is made by added an author to book structure. The book is given by ID.

<p align="center">
  <img src="pictures/book structure.png" alt="book structure">
</p>

<br>

<p align="center">
  <img src="pictures/addAuthorToBook function.png" alt="addAuthorToBook function">
</p>

#### Verify if the user that wants to link is admin 
The following three aspects are checked here: the existence of the token, its validity, and the user's role being ADMIN. Otherwise, the following errors are thrown in this order: `UserNotLoggedInException`, `InvalidTokenException`, `UnauthorizedUserException`.

<p align="center">
  <img src="pictures/jwtUtil.png" alt="jwtUtil">
</p>

#### Verify if book exists and if is deleted
It is checked whether a book with the given ID exists in the database. If affirmative, the book is retained for modification; otherwise, a `NoSuchElementException` is thrown with the message "Book with this id not found."
If the book has been soft-deleted, it cannot be modified because it is assumed to be deleted, meaning it no longer exists. In this case, a `DeletedBookException` is thrown with the error message "Cannot add author to a deleted book.".

<p align="center">
  <img src="pictures/getUserById.png" alt="getUserById">
</p>

#### Save the author
The author is saved only if that author does not already exist in the database. Therefore, it is checked whether there is already one with the same first and last name. In this case, the author retrieved from the database is returned. Otherwise, the new author is inserted into the database, and then it is returned.

<p align="center">
  <img src="pictures/saveAuthor.png" alt="saveAuthor">
</p>

#### Link the author to the book
At this point, we have the book and the author, and it remains only to specify that the author of the respective book is the one provided.
The updated book is returned.

</details>


<details><summary><b>Insert a book or increase the number of copies of a book in a basket feature</b></summary>

The feature is implemented through a single function because increasing the number of copies means starting with a single copy and incrementing the count. Therefore, if that book is not in the basket, it will be added, and if it exists, the count will be increased.

The parameters provided are the token, the book's ID, and the basket's ID.

<p align="center">
  <img src="pictures/addBookInBasket_BookService.png" alt="addBookInBasket_BookService">
</p>

#### Verify if the user is logged in
The following three aspects are checked here: the existence of the token and its validity. Otherwise, the following errors are thrown in this order: `UserNotLoggedInException`, `InvalidTokenException`.

<p align="center">
  <img src="pictures/decode_token.png" alt="decode_token">
</p>
<p align="center">
  <img src="pictures/verify_if_loggedin.png" alt="verify_if_loggedin">
</p>

#### Verify if basket exists
The code is straightforward; it checks if there is a basket in the database with the given ID. If affirmative, that value is retained for future modifications. If not, a `NoSuchElementException` is thrown with the message "Does not exist a basket with this id."


#### Insert/increment the book in basket
Moving forward, we need to consider the relationship between that book and the basket. Because it involves another table, a function with the same name is created in the service of the linking table. First and foremost, it checks if there is indeed a book with that given ID as a parameter. If yes, it checks whether that book is already in the basket. If not, a single copy is added to the basket; otherwise, the copy count is incremented by 1, and the modification is saved. The function returns the price of that book to add it later to the total price of the basket.

<p align="center">
  <img src="pictures/addBookInBasket_BookBaskeService.png" alt="addBookInBasket_BookBasketService">
</p>

### Increase in price of the basket and save the changes
In the end, the returned price is added to the total price of the basket, the modification is saved, and the user's basket is returned.

</details>


<details><summary><b>Reducing the number of copies of a book from the basket or deleting the basket</b></summary>

This feature is implemented through two different functions because the logic differs from the add-increment scenario. Incrementing means having a base count; if it doesn't exist, it is added. At the same time, if a book is added that already exists in the basket, its count is increased, i.e., incremented.

On the other hand, now if I want to delete, I have to remove all copies, and to decrement, I have to remove just one (decrease the count). Let's assume that in the frontend, we have a "delete from basket" button. I can't decrease by 1 when the removal is requested. If, on the other hand, decrementing is desired, it is checked if there is only one copy, and if so, it is removed; otherwise, only one is subtracted.

<p align="center">
  <img src="pictures/removeBookFromBasket_BasketService.png" alt="removeBookFromBasket_BasketService">
</p>
<p align="center">
  <img src="pictures/decrementBookToBasket_BasketService.png" alt="decrementBookToBasket_BasketService">
</p>

#### Verify if the user is logged in (common part)

The user's role does not matter; what's important is that they have an account to place an order. Therefore, the first step is to check if they are logged in, meaning if there is a valid token.

<p align="center">
  <img src="pictures/decode_token.png" alt="decode_token">
</p>
<p align="center">
  <img src="pictures/verify_if_loggedin.png" alt="verify_if_loggedin">
</p>

#### Verify if basket exists (common part)
The code is straightforward; it checks if there is a basket in the database with the given ID. If affirmative, that value is retained for future modifications. If not, a `NoSuchElementException` is thrown with the message "Does not exist a basket with this id."

#### Remove book from basket
In the service of the associative table between books and baskets, the `removeBookToBasket` function was created. It takes the IDs of the book and the basket, checks if that book is in the basket, and if so, removes that book from the basket and returns the price of that book in the basket (number of books multiplied by the price of one book). If not, a `NoSuchElementException` is thrown with the message "The book is not in this basket."

<p align="center">
  <img src="pictures/removeBookToBasket_bookBasket.png" alt="removeBookToBasket_bookBasket">
</p>

#### Decrement book from basket
In the service of the associative table between books and baskets, the `decrementBookFromBasket` function was created. It takes the IDs of the book and the basket, checks if that book is in the basket, and if so, removes that book from the basket if there was only one copy in the basket. If there are multiple copies, it decrements the count and saves the modification. In both cases, it returns the price of that book in the basket (number of books multiplied by the price of one book). If not, a `NoSuchElementException` is thrown with the message "The book is not in this basket."

<p align="center">
  <img src="pictures/decrementBookToBasket_BookBasket.png" alt="decrementBookToBasket_BookBasket">
</p>

#### Decrease in price of the basket and save the changes (common part)
After returning the price, in both functions, the returned price is subtracted from the total price of the basket, the modification is saved, and the basket is returned.

</details>


<details><summary><b>Send order feature</b></summary>

The function receives a token and a user ID as parameters and returns the basket for which the order has been placed.
<p align="center">
  <img src="pictures/sentOrder.png" alt="sentOrder">
</p>

#### Verify if the user is logged in

The user's role does not matter; what's important is that they have an account to place an order. Therefore, the first step is to check if they are logged in, meaning if there is a valid token.

<p align="center">
  <img src="pictures/decode_token.png" alt="decode_token">
</p>
<p align="center">
  <img src="pictures/verify_if_loggedin.png" alt="verify_if_loggedin">
</p>

#### Verify if an order can be sent
In order to place an order, the user identified by their ID must have a basket (otherwise, a NoSuchElementException is thrown with the message "User does not have a current basket"), and this basket must contain at least one item (book). An order cannot be placed on an empty basket. The emptiness of the basket is checked by examining its total price. Assuming there are no books with a negative or zero price, if the total price is different from zero, it means there are books; otherwise, the same error is thrown again, but this time with the message "User does not have books in the basket."

#### Make order
After these steps, the order is fulfilled by setting the "sent" field in the basket to true. Thus, the order is assumed to be completed, and the next time a book is added, a new basket will be created. The modification is saved, and the user's basket is returned.

</details>

<details><summary><b>Admin can manage categories and authors</b></summary>

Management of categories and authors is similar. The user must have admin rights, meaning the ADMIN role, which will require a valid and authorized token..

#### Create functionality
In the create method, it is checked whether the new object to be inserted is new or a repeated one. For authors, it checks if there is already another author in the database with the same first and last name, and for categories, it checks if there is another one with the same category name. If affirmative, a new instance will not be added; instead, the existing one will be retained and returned.

#### Update functionality
For updating an object, it will first check if an object with that ID exists in the database; otherwise, it throws a NoSuchElementException with a suggestive message.

In the case of authors, it updates the first name, last name, and nationality. For categories, it updates the category name. If a field is omitted in the JSON body, it is filled with null. The assumption is that in the case of an application with a frontend, all specified fields will be sent to the backend for execution, whether they have been modified or not.

#### Delete functionality
A hard delete with ON CASCADE is performed to delete the link with the books as well. There is no need for a soft delete because this data does not need to be retained, unlike data about books that may need to be kept for tracking past orders even after deletion.
</details>


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

<details><summary><b>Folder with all the controller and service tests</b></summary>
<p align="center">
  <img src="pictures/folder_tests.png" alt="folder_tests">
</p>
</details>

<details><summary><b>Tests passed for controllers</b></summary>
<br>

<details><summary><i>AuthorControllerTest</i></summary>
    <p align="center">
      <img src="pictures/authorControllerTest.png" alt="authorControllerTest">
    </p>
</details>

<details><summary><i>BasketControllerTest</i></summary>
    <p align="center">
      <img src="pictures/basketControllerTest.png" alt="basketControllerTest">
    </p>
</details>
    
<details><summary><i>BookControllerTest</i></summary>
    <p align="center">
      <img src="pictures/bookControllerTest.png" alt="bookControllerTest">
    </p>
</details>
    
<details><summary><i>CategoryControllerTest</i></summary>
    <p align="center">
      <img src="pictures/categoryControllerTest.png" alt="categoryControllerTest">
    </p>
</details>

<details><summary><i>UserControllerTest</i></summary>
    <p align="center">
      <img src="pictures/userControllerTest.png" alt="userControllerTest">
    </p>
</details>
<br>

</details>


<details><summary><b>Tests passed for services</b></summary>
<br>

<details><summary><i>AuthorServiceTest</i></summary>
    <p align="center">
      <img src="pictures/authorServiceTest.png" alt="authorServiceTest">
    </p>
</details>

<details><summary><i>BasketServiceTest</i></summary>
    <p align="center">
      <img src="pictures/basketServiceTest.png" alt="basketServiceTest">
    </p>
</details>

<details><summary><i>BookServiceTest</i></summary>
    <p align="center">
      <img src="pictures/bookServiceTest.png" alt="bookServiceTest">
    </p>
</details>

<details><summary><i>BookBasketServiceTest</i></summary>
    <p align="center">
      <img src="pictures/bookBasketServiceTest.png" alt="bookBasketServiceTest">
    </p>
</details>

<details><summary><i>CategoryServiceTest</i></summary>
    <p align="center">
      <img src="pictures/categoryServiceTest.png" alt="categoryServiceTest">
    </p>
</details>

<details><summary><i>UserServiceTest</i></summary>
    <p align="center">
      <img src="pictures/userServiceTest.png" alt="userServiceTest">
    </p>
</details>
<br>

</details>


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

<details><summary><b>Examples of functionalities documentation in swagger</b></summary>
<br>
<details><summary><i>Update an author details functionality</i></summary>
<p align="center">
  <img src="pictures/update author functionality.png" alt="update author functionality">
</p>
</details>


<details><summary><i>Link an author to a book functionality </i></summary>

<p align="center">
  <img src="pictures/addAuthorToBook functionality.png" alt="addAuthorToBook functionality">
</p>
</details>
<br>

</details>

## <i>The functionality of the application will be demonstrated using Postman</i>

To add a parameter in the request header using *@RequestHeader* you should write 
> **pm.request.headers.add("foo: bar");**
> 
> where:<br>
> **foo** is the key <br>
> **bar** is the value

> **ADMIN VERSION**:
> 
> pm.request.headers.add("userToken: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZW5pc2FQcmVkZXNjdSIsInJvbGUiOiJBRE1JTiJ9.ZA0vxSE7keltGZWcNYlRTor-TBOXOrUxFbCsUleok4Y");

> **CUSTOMER version**:
>
> pm.request.headers.add("userToken: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VydXNlciIsInJvbGUiOiJVU0VSIn0.lkIf0276-9gS9nk9M_VEgoabl39m9qYIZuPa5zP4vpg");

#### ADMIN TOKEN
<p align="center">
  <img src="pictures/request header example.png" alt="request header example">
</p>

#### CUSTOMER (NOT ADMIN) TOKEN
<p align="center">
  <img src="pictures/request header for customer role.png" alt="request header for customer role">
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

<details><summary><b>Handler class</b></summary>

<p align="center">
  <img src="pictures/handler_1.png" alt="handler_1">
</p>

<br>

<p align="center">
  <img src="pictures/handler_2.png" alt="handler_2">
</p>
</details>

<details><summary><b>How exceptions are thrown examples</b></summary>

<p align="center">
  <img src="pictures/thrown_exception_deletedBookException.png" alt="deletedBookException">
</p>

<br>

<p align="center">
  <img src="pictures/thrown_exception_noSuchElement.png" alt="noSuchElement">
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