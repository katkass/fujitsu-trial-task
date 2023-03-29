package com.rait.fujitsutrialtask;

import com.rait.fujitsutrialtask.model.Weather;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Specific handler class for the weather data that extends the default handler class.
 * SAX code blueprint: https://www.digitalocean.com/community/tutorials/java-sax-parser-example &
 * http://www.saxproject.org/quickstart.html.
 */
public class WeatherHandler extends DefaultHandler {

    /**
     * 2038 compatible timestamp.
     */
    private final long timestamp = System.currentTimeMillis() / 1000L;

    /**
     * List to hold Weather plain-old Java objects.
     */
    private List<Weather> weatherList = null;
    private Weather weatherData = null;
    private StringBuilder data = null;

    /**
     * Returns the list of Weather objects.
     *
     * @return a list of Weather objects
     */
    public List<Weather> getWeatherList() {

        return weatherList;
    }

    /**
     * Specified stations to update (Tallinn, Tartu, Pärnu). Avoids adding the entire XML list.
     * Can in theory be added to.
     *
     * TODO: Make this a table in the database instead.
     */
    private List<Integer> stationsToUpdate = List.of(26038, 26242, 41803);
    public List<Integer> getCurrentlyUpdatedStations(){

        return stationsToUpdate;
    }
    public void addStationToUpdateSchedule(int WMOCode){

        if(!this.stationsToUpdate.contains(WMOCode)){ this.stationsToUpdate.add(WMOCode); }
    }


    /**
     * The required data fields from the XML file.
     */
    boolean bWMOCode = false;
    boolean bStation = false;
    boolean bAirTemp = false;
    boolean bWindSpeed = false;
    boolean bPhenomenon = false;

    /**
     * Receive notification of the beginning of an element. Report the content.
     *
     * @param uri the namespace URI
     * @param localName the local name
     * @param qName the qualified (prefixed) name
     * @param attributes the attributes attached to the element
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        //Create a new weatherList when you parse the first station.
        if (qName.equalsIgnoreCase("station") && weatherList == null){

            weatherList = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("name")) {

            //Set boolean values for fields, will be used in filling out data fields.
            bStation = true;
        } else if (qName.equalsIgnoreCase("wmocode")) {

            bWMOCode = true;
        } else if (qName.equalsIgnoreCase("airtemperature")) {

            bAirTemp = true;
        } else if (qName.equalsIgnoreCase("windspeed")) {

            bWindSpeed = true;
        } else if (qName.equalsIgnoreCase("phenomenon")) {

            bPhenomenon = true;
        }

        //Create the data container.
        data = new StringBuilder();
    }

    /**
     * Receive notification of the beginning of an element. Report the content.
     *
     * @param uri the namespace URI
     * @param localName the local name
     * @param qName the qualified (prefixed) name
     */
    @Override
    public void endElement(String uri, String localName, String qName) {

        //If there is a new station tag, create a new Weather object.
        if (bStation) {

            //Also set the timestamp. There is a timestamp in the XML file, but I can't
            //figure out how to get to it, since it is inside a tag.
            //TODO: Get the embedded timestamp from the XML file.
            weatherData = new Weather();
            weatherData.setTimestamp(timestamp);

            //Strip trailing whitespace and get rid a of formatting error at Kaev-219.
            //I should probably strip all of them to avoid any parsing errors.
            weatherData.setStation(data.toString().stripTrailing());

            //Reset for next tag.
            bStation = false;
        } else if (bWMOCode) {

            //If it isn't a new station, add the content from the inner tags instead.
            //If that doesn't exist, don't add it.
            try {

                weatherData.setWMOCode(Integer.parseInt(data.toString()));
            }
            //Either way reset for same element in next station.
            catch (Exception e) {

                bWMOCode = false;
            }

            bWMOCode = false;
        } else if (bAirTemp) {

            try {

                weatherData.setAirTemp(Float.parseFloat(data.toString()));
            }
            catch (Exception e) {

                bAirTemp = false;
            }

            bAirTemp = false;
        } else if (bWindSpeed) {

            try {

                weatherData.setWindSpeed(Float.parseFloat(data.toString()));
            }
            catch (Exception e) {

                bWindSpeed = false;
            }

            bWindSpeed = false;
        } else if (bPhenomenon) {

            weatherData.setPhenomenon(data.toString());
            bPhenomenon = false;
        }

        //If we get to the next station element having gone through the content,
        //and the station is on the list of stations to be added, do so.
        if (qName.equalsIgnoreCase("station") &&
            stationsToUpdate.contains(weatherData.getWMOCode())) {

            weatherList.add(weatherData);
        }
    }

    /**
     * Receive notification of character data inside an element and append that to our
     * data String to be added to the respective field in our Weather object.
     *
     * @param ch the whitespace characters
     * @param start the start position in the character array
     * @param length the number of characters to use from the character array
     */
    @Override
    public void characters(char[] ch, int start, int length) {

        data.append(new String(ch, start, length));
    }
}