package com.rait.fujitsutrialtask;

import com.rait.fujitsutrialtask.model.Weather;
import com.rait.fujitsutrialtask.dao.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

/**
 * The DatabaseLoader class implements the CommandLineRunner interface to instantiate data both to the
 * database during startup and as a scheduled ChronJob while the app is running. These are both done
 * using the same method. The data is accessed through a repository interface.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    /**
     * Logger for console messages and set up date format.
     */
    private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * Set up repository for weather data.
     */
    private WeatherRepository weatherRepository;
    public DatabaseLoader(WeatherRepository weatherRepository) {

        this.weatherRepository = weatherRepository;
    }

    /**
     * Run the pullNewWeatherList() method hourly, 15 minutes past the hour, as a ChronJob.
     *
     * @throws ParserConfigurationException throws parser config exception
     * @throws SAXException throws SAX exception
     * @throws IOException throws I/O exception
     */
    @Scheduled(cron="0 15 * * * *")
    public void scheduledWeatherUpdate() throws ParserConfigurationException,
            SAXException, IOException {

        //Save new weather data to database.
        pullNewWeatherList();
    }

    /**
     * Get XML data from a specified URL and parse it through SAX, using an instance of the
     * WeatherHandler class to format the parsed data in the required way into a list of
     * Weather entities. These will be saved into the repository. Run both at app start and
     * using the scheduled task.
     *
     * @throws ParserConfigurationException throws parser config exception
     * @throws SAXException throws SAX exception
     * @throws IOException throws I/O exception
     */
    private void pullNewWeatherList() throws IOException, SAXException,
            ParserConfigurationException {

        //Instantiate a new SAX parser and our handler.
        WeatherHandler handler = new WeatherHandler();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        SAXParser saxParser = saxParserFactory.newSAXParser();

        //Parsing from URL using that handler.
        //Source for live weather data: Keskkonnaagentuur (ilmateenistus.ee).
        saxParser.parse("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php", handler);

        //Getting the list of weather entities from the handler and saving them to the repository.
        List<Weather> weatherList = handler.getWeatherList();
        weatherList.forEach(this.weatherRepository::save);

        //Log time and newly saved data.
        log.info("");
        log.info("The time is now {}", dateFormat.format(new Date()));
        weatherList.forEach((weatherdata) -> log.info("{}", weatherdata));

    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws ParserConfigurationException throws parser config exception
     * @throws SAXException throws SAX exception
     * @throws IOException throws I/O exception
     */
    @Override
    public void run(String... args) throws ParserConfigurationException, SAXException, IOException {

        //this.weatherRepository.deleteAll();
        /*
        Weather tooWindy = new Weather("Tallinn-Harku", 26038, -2.9000000953674316,
                21.100000023841858, "Few clouds", 1781781791);
        this.weatherRepository.save(tooWindy);
         */

        //Print the contents of the database at startup.
        this.weatherRepository.findAll().forEach((weatherdata) -> log.info("{}", weatherdata));

        //Save new weather data to database.
        pullNewWeatherList();
    }
}
