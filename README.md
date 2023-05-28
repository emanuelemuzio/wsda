# 0766230 - Emanuele Muzio

### General Info

Documentation for the *Web Systems Design and Architecture* uni project.  

## Functional Requirements

The application must be hable to simulate the handling of pre-paid credit cards.  
The required functionalities are:

1. Creating new credit cards
2. Check card's balance
3. Simulate a purchase
4. Simulate a recharge

## Technical Requirements

### Architecture

1. Application data must be stored in a relational DB (MySQL is fine)
2. The app must distinguish 2 different Utente types: admin Utente, which has access to functionalities 1, 3 and 4, and merchant Utente, who accesses functionalities 3 and 4.
3. Functionality n.2 doesn't require the Utente to be authenticated, as it is available to anyone who possess the card number.


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
* Maven 4.0
* Apache Tomcat 10 (Embedded web server in Spring Boot)
* PostgreSQL (also loaded into Spring Boot dependencies)

### Tools, dependencies and external libs

* [initializr](https://github.com/spring-io/initializr) for initializing the project structure
* IntelliJ Idea Ultimate (which already integrates the initializr)
* [Jackson](https://github.com/FasterXML/jackson) for handling JSONs in Java
* Docker (for running the PostgreSQL DB)
* Spring Data JPA for accessing database
* Bootstrap 5.1.3
* jQuery 3.7.0
* Thymeleaf template engine

# Da sistemare

* import.sql permette di generare degli utenti di default all'avvio del progetto




