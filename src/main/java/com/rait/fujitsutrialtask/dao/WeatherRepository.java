package com.rait.fujitsutrialtask.dao;

import com.rait.fujitsutrialtask.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for interacting with weather data entities in the database.
 */
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    /**
     * Gets weather data by matching WMO code sorted by timestamp, in essence the latest weather
     * data for a given city.
     *
     * @param WMOCode WMO code for the given city
     * @return the latest weather data for the given city
     */
    List<Weather> findFirstByWMOCodeOrderByTimestampDesc(int WMOCode);

    /**
     * Gets weather data by matching WMO code and specific timestamp if that field is filled
     * at query.
     *
     * @param WMOCode WMO code for the given city
     * @param Timestamp Timestamp to search for
     * @return specific weather data for a given city at a timestamp
     */
    List<Weather> findByWMOCodeAndTimestamp(int WMOCode, long Timestamp);
}
