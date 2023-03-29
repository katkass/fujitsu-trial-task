package com.rait.fujitsutrialtask.controller;

import com.rait.fujitsutrialtask.model.Fee;
import com.rait.fujitsutrialtask.model.Weather;
import com.rait.fujitsutrialtask.dao.WeatherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rait.fujitsutrialtask.model.Weather.CAR;

/**
 * This controller trying to implement something resembling a RESTful connection between
 * the database through the WeatherRepository to the HTML front-end.
 *
 * It sort of works, but I hate it and will need to burn it all down and do it again
 * now that I understand on some level how Java + Spring + H2 with some REST, CRUD ought to work.
 */
@Controller
public class HTMLController {

    //Repository to interface between the database and front-end.
    private final WeatherRepository repository;
    public HTMLController(WeatherRepository repository) {
        this.repository = repository;
    }

    /**
     * A model to which two objects are added to get data passed between requests
     * and responses. GetMapped, which is a request. The user enters their data
     * into the form.
     *
     * @param model supplies attributes used for rendering views
     * @return a view
     */
    @GetMapping("/")
    public String form(Model model){

        model.addAttribute("weather", new Weather());
        model.addAttribute("fee", new Fee());

        return "form";
    }

    /**
     * The corresponding response. Difigured by the doing the calculations directly in
     * the method. Does result in the correct number.
     *
     * @param weather the weather object data
     * @param fee the fee object data
     * @param model supplies attributes used for rendering views
     * @return a view
     */
    @PostMapping("/processForm")
    public String result(@ModelAttribute("weather") Weather weather,
                         @ModelAttribute("fee") Fee fee, Model model){

        if(weather.getTimestamp() != 0L){

            List<Weather> list = specific(weather.getWMOCode(), weather.getTimestamp());
            if(!list.isEmpty()){
                weather = list.get(0);
            }
            else {

                return "error";
            }
        }
        else {

            weather = latest(weather.getWMOCode());
        }

        //Before calculating fee, check for illegal combinations of vehicle type and weather
        if ((fee.getVehicleType() != CAR) && weather.checkIllegal(fee.getVehicleType())){

            //Give forbidden combination message instead.
            //TODO: This just gives the error, which means it works, but I would like a different page.
            return "redirect:forbidden";
        }

        //Get total fee based on vehicle type, city rate, and weather conditions.
        fee.setTotal(weather.getTotalFee(fee.getVehicleType()));

        model.addAttribute("weather", weather);
        model.addAttribute("fee", fee);

        return "result";
    }

    /**
     * Returns a weather object from the database through the repository interface
     * matching the condition set. Gets the latest weather data for a specific city.
     *
     * @param WMOCode station WMO code
     * @return latest weather object for corresponding city
     */
    @GetMapping("/latest/{WMOCode}")
    @ResponseBody
    public Weather latest(@PathVariable int WMOCode){

        return repository.findFirstByWMOCodeOrderByTimestampDesc(WMOCode).get(0);
    }

    /**
     * Returns a weather object from the database through the repository interface
     * matching the condition set. Gets the latest weather data object list for a
     * specific city at a specific timestamp. List empty if not found.
     *
     * @param WMOCode station WMO code
     * @param Timestamp corresponding time
     * @return list of corresponding weather objects
     */
    @GetMapping("/latest/{WMOCode}/specific/{Timestamp}")
    @ResponseBody
    public List<Weather> specific(@PathVariable int WMOCode, @PathVariable long Timestamp){

        return repository.findByWMOCodeAndTimestamp(WMOCode, Timestamp);
    }

}