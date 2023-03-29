package com.rait.fujitsutrialtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main class which makes the app run.
 *
 * The app consists of a DatabaseLoader class to instantiate a database (and fill it periodically),
 * using a SAX parser and the WeatherHandler handler class to parse an XML file for data from an URL.
 * This data is stored as Weather java objects. These are accessed by the RESTful HTML controller's
 * CRUD operations through the WeatherRepository interface.
 *
 * This barely results in a functional REST API connecting a database to a front-end.
 *
 */
@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}

}
