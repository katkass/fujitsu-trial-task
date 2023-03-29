package com.rait.fujitsutrialtask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;

/**
 * Plain-old Java object representing a weather station with data fields corresponding to a
 * parsed XML element. Table and columns assigned using corresponding tags instead of schema.sql
 * file.
 */
@Entity
@Table
public class Weather {

    //Some static variables.
    public static final int TALLINN = 26038;
    public static final int TARTU = 26242;
    public static final int PARNU = 41803;
    public static final double TALLINN_SURCHARGE = 1;
    public static final double TARTU_SURCHARGE = 0.5;
    public static final double ATEF_MIN_TEMP = -10;
    public static final double ATEF_MAX_TEMP = 0;
    public static final double ATEF_MIN_CHARGE = 1;
    public static final double ATEF_MAX_CHARGE = 0.5;
    public static final double WPEF_SNOW_OR_SLEET = 1;
    public static final double WPEF_RAIN = 0.5;
    public static final double WSEF_BETWEEN_LIMIT = 0.5;
    public static final double WSEF_MIN = 10;
    public static final double WSEF_MAX = 20;
    public static final double CAR = 3;
    public static final double SCOOTER = 2.5;
    public static final double BIKE = 2;

    /**
     * Data fields.
     */
    private @Id @GeneratedValue Long id; //Primary key, auto-populated
    private @Column String station; //Name of the station
    private @Column int WMOCode; //WMO code of the station
    private @Column double airTemp; //Air temperature
    private @Column double windSpeed; //Wind speed
    private @Column String phenomenon; //Weather phenomenon
    private @Column long timestamp; //Timestamp of the observations

    /**
     * Constructors for instantiation.
     */
    public Weather() {}
    public Weather(String station, int WMOCode, double airTemp,
                   double windSpeed, String phenomenon, int timestamp) {

        this.station = station;
        this.WMOCode = WMOCode;
        this.airTemp = airTemp;
        this.windSpeed = windSpeed;
        this.phenomenon = phenomenon;
        this.timestamp = timestamp;
    }

    /**
     * Getter for weather object ID.
     *
     * @return object ID
     */
    public Long getId() {
        return id;
    }
    /**
     * Setter for weather object ID.
     *
     * @param id ID code
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for weather object station name.
     *
     * @return object station name
     */
    public String getStation(){
        return station;
    }
    /**
     * Setter for weather object station name.
     *
     * @param station station name
     */
    public void setStation(String station) {
        this.station = station;
    }

    /**
     * Getter for weather object WMO code.
     *
     * @return object WMO code
     */
    public int getWMOCode(){
        return WMOCode;
    }
    /**
     * Setter for weather object WMO code.
     *
     * @param WMOCode station WMO code
     */
    public void setWMOCode(int WMOCode) {
        this.WMOCode = WMOCode;
    }

    /**
     * Getter for weather object air temperature.
     *
     * @return object air temperature
     */
    public double getAirTemp(){
        return airTemp;
    }
    /**
     * Setter for weather object station name.
     *
     * @param airTemp station air temperature
     */
    public void setAirTemp(float airTemp) {
        this.airTemp = airTemp;
    }

    /**
     * Getter for weather object wind speed.
     *
     * @return object wind speed
     */
    public double getWindSpeed() {
        return windSpeed;
    }
    /**
     * Setter for weather object wind speed.
     *
     * @param windSpeed station wind speed
     */
    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * Getter for weather object weather phenomenon.
     *
     * @return object weather phenomenon
     */
    public String getPhenomenon() {
        return phenomenon;
    }
    /**
     * Setter for weather object weather phenomenon.
     *
     * @param phenomenon station weather phenomenon
     */
    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    /**
     * Getter for weather object query timestamp (UNIX time).
     *
     * @return object query timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }
    /**
     * Setter for weather object query timestamp (UNIX time).
     *
     * @param timestamp station query timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * See if there are any illegal combinations of weather and vehicle possible.
     *
     * @param vehicleType type of vehicle, bike or scooter
     * @return true if illegal, false if not
     */
    public Boolean checkIllegal(double vehicleType){
        //Weather phenomena of glaze, hail, or thunder, are illegal on bike and scooter.
        if((vehicleType != CAR) && (this.phenomenon.contains("glaze") ||
                this.phenomenon.contains("hail") || this.phenomenon.contains("thunder"))){
            return true;
        }

        //Wind speed greater than 20 is illegal on a bike.
        if(this.windSpeed > WSEF_MAX && vehicleType == BIKE){
            return true;
        }

        return false;
    }

    /**
     * Gives total fee according to vehicle type, city rate, and current weather conditions.
     *
     * @return total fee
     */
    public double getTotalFee(double RBF){

        //Store to see if car or scooter before base rate increase.
        double notABike = RBF;

        //Environmental charges.
        double ATEF = 0;
        double WPEF = 0;
        double WSEF = 0;

        //Increase vehicle base rate if Tallinn or Tartu.
        if(this.WMOCode == TALLINN){ RBF += TALLINN_SURCHARGE; }
        else if(this.WMOCode == TARTU){ RBF += TARTU_SURCHARGE; }

        //Cars not affected by weather, return total fee immediately.
        if(notABike == CAR){ return RBF; }

        //Air temp cost (if scooter or bike).
        if(this.airTemp < ATEF_MIN_TEMP){ ATEF = ATEF_MIN_CHARGE; } //Less than -10, ATEF = 1
        else if (this.airTemp <= ATEF_MAX_TEMP){ ATEF = ATEF_MAX_CHARGE; } //Else between -10 and 0 ATEF = 0.5

        //Weather phenomenon (if scooter or bike):
            if(phenomenon.contains("snow") || phenomenon.contains("sleet")) {
                WPEF = WPEF_SNOW_OR_SLEET; //Snow or sleet, WPEF = 1
            }
            else if (phenomenon.contains("rain")){
                WPEF = WPEF_RAIN; //Rain, WPEF = 0.5
            }

        //If scooter, no wind speed cost, return total fee.
        if(notABike == SCOOTER){ return RBF + ATEF + WPEF; }

        //Wind speed cost (if bike).
        if(this.windSpeed >= WSEF_MIN && this.windSpeed <= WSEF_MAX){
            WSEF = WSEF_BETWEEN_LIMIT; }  //Between 10 and 20, WSEF = 0.5

        //Total worst possible delivery fee (on a bike) = RBF + ATEF + WSEF + WPEF
        return RBF + ATEF + WSEF + WPEF;
    }

    /**
     * Returns the information about the given weather conditions
     * at a station at a certain timestamp as a formatted string.
     *
     * @return the station information
     */
    @Override
    public String toString(){
        return "Station: " + this.station + " WMO: " + this.WMOCode + " TEMP: " + this.airTemp + " SPEED: " +
                this.windSpeed + " WEATHER: " + this.phenomenon + " TIME: " + this.timestamp;
    }

}
