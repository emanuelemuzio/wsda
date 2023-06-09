# 0766230 - Emanuele Muzio

### General Info

Documentation for the *Web Systems Design and Architecture* uni project.  

## Functional Requirements

The application must be hable to simulate the handling of pre-paid credit cards.  
The application will differentiate 3 user types:

1. Admin (only one for the entire project)  
2. Merchant (one store has many merchants)  
3. Credit card owner  

The required functionalities are:

1. Creating new credit cards
2. Check card's balance
3. Simulate a purchase
4. Simulate a recharge

Everyone can check a card's balance given the number.  

After the login:

1. A merchant can make a charge or recharge a card given the number
2. An admin can create a new card and block/unblocka a card given the number

Optional:

1. A merchant can generate a report containing the past transactions.
2. An admin can disable or enable merchants
3. An admin can generate reports with various filters
4. A card owner can see the latest transactions and generate a report

## Technical Requirements

### Architecture

1. Application data must be stored in a relational DB (MySQL is fine)
2. The app must distinguish 2 different user types: Merchant User, which has access to functionalities 3 and 4, and Admin User, who accesses functionalities 1, 3 and 4.
3. Functionality n.2 doesn't require the user to be authenticated, as it is available to anyone who possess the card number.


### Backend

1. Java EE application server
2. Plain servlet/JSP

Or

1. Java Spring Boot

### Frontend

1. Html
2. Css
3. JavaScript
4. jQuery

Now that the few requirements have been listed, let's proceed to the project's specs

## Project Specifics

### Architecture

* Java 17
* Spring Boot 3.1.0
* Maven 4.0 for dependencies 
* Apache Tomcat 10 (Embedded web server in Spring Boot)
* PostgreSQL 

### Tools, dependencies and external libs

* Hibernate for managing JPA (Jakarta Persistance API)
* [initializr](https://github.com/spring-io/initializr) for initializing the project structure
* IntelliJ Idea Ultimate (which already integrates the initializr)
* [Jackson](https://github.com/FasterXML/jackson) for handling JSONs in Java
* Docker (for running the PostgreSQL DB)
* Spring Data JPA for accessing database
* Bootstrap 5.1.3
* jQuery 3.7.0
* Thymeleaf template engine