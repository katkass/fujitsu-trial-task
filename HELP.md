# Read Me First

Web access is default, localhost:8080.

#What we will look at:
• The OOP design of the solution, layering, clean code, common patterns, etc

• That code is human readable

• Public methods should be documented

• Error handling

• Test coverage

• Where code is stored and how work is organized


 #Technologies to use
• Java

• Spring framework

• H2 database

#Description of the task

The objective of the task is to develop a sub-functionality of the food delivery application, which
calculates the delivery fee for food couriers based on regional base fee, vehicle type, and weather
conditions.

Core modules to implement:

• Database for storing and manipulating data X

• Configurable scheduled task for importing weather data (CronJob) X

• Functionality to calculate delivery fee X

• REST interface, which enables to request of the delivery fee according to input parameters X

Stand out:

• Solve the delivery fee calculation and business rules related to the process in a way
that business rules for base fees and extra fees could be managed (CRUD) through the REST
interface

• You can add additional datetime parameters to the REST interface request. This parameter
should not be mandatory, but if it’s valued, delivery fee calculations have to be done based on
business rules and weather conditions, which were valid at the specific time X

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#using.devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

