package com.epam.brest.courses.web_app;

import com.epam.brest.courses.model.Car;
import com.epam.brest.courses.service_api.CarDtoService;
import com.epam.brest.courses.service_api.CarService;
import com.epam.brest.courses.web_app.validators.CarValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Car web controller.
 */
@Controller
public class CarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

    private CarValidator carValidator = new CarValidator();

    private final CarService carService;

    private final CarDtoService carDtoService;

    @Autowired
    public CarController(CarService carService, CarDtoService carDtoService) {
        this.carService = carService;
        this.carDtoService = carDtoService;
    }

    /**
     * Goto free cars list.
     *
     * @param model model
     * @return view name
     */
    @GetMapping(value = "/cars")
    public final String freeCars( @RequestParam(name = "filter", required = false) String filter, Model model) throws ParseException {
        LOGGER.debug("free cars on date: {}", filter);
        Date dateNow = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

        if (filter == null || filter.length() == 0) {
            filter = formatDate.format(dateNow);
        }

        Date filterDate = formatDate.parse(filter);
        if(filterDate.before(dateNow)){
            filter = formatDate.format(dateNow);
        }

        List<Car> cars = carService.findAllByDate(filter);
        model.addAttribute("filter", filter);
        model.addAttribute("cars",cars);
        return "cars";
    }

    /**
     * Goto edit car page.
     *
     * @param id    car
     * @param model model
     * @return view name
     */
    @GetMapping(value = "/car/{id}")
    public final String gotoEditCarPage(@PathVariable Integer id, Model model) {
        LOGGER.debug("gotoEditCarPage({},{})", id, model);

        Optional<Car> car = carService.findById(id);

        if (car.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("car", car.get());
            return "car";
        } else {
            return "redirect:/cars";
        }
    }

    /**
     * Update car.
     *
     * @param car    to be updated
     * @param result binding result
     * @return redirect to view name
     */
    @PostMapping(value = "/car/{id}")
    public final String updateCar(@Valid Car car, BindingResult result) {
        LOGGER.debug("updateCar({}, {})", car, result);

        carValidator.validate(car, result);
        if (result.hasErrors()) {
            return "car";
        } else {
            carService.update(car);
            return "redirect:/cars";
        }
    }

    /**
     * Goto create car page.
     *
     * @param model model
     * @return view name
     */
    @GetMapping(value = "/car")
    public final String gotoCreateCarPage(Model model) {
        LOGGER.debug("gotoCreateCarPage({})", model);

        model.addAttribute("isNew", true);
        model.addAttribute("car", new Car());
        return "car";
    }

    /**
     * Create new car.
     *
     * @param car    new car with filled data
     * @param result binding result
     * @return redirect to view name
     */
    @PostMapping(value = "car")
    public final String createCar(@Valid Car car, BindingResult result) {
        LOGGER.debug("createCar({}, {})", car, result);

        carValidator.validate(car, result);
        if (result.hasErrors()) {
            return "car";
        } else {
            carService.create(car);
            return "redirect:/cars";
        }
    }

    /**
     * Delete car.
     *
     * @param id car
     * @return view name
     */
    @GetMapping(value = "car/{id}/delete")
    public final String deleteCar(@PathVariable Integer id) {
        LOGGER.debug("deleteCar({})", id);

        carService.delete(id);
        return "redirect:/cars";
    }

    /**
     * Show cars with number of orders.
     *
     * @param dateFrom date from
     * @param dateTo date to
     * @param model model
     * @return view name
     */
    @GetMapping(value = "/car-statistics")
    public String carStatistics(@RequestParam(value="dateFrom",required = false) String dateFrom
            ,@RequestParam(value = "dateTo",required = false) String dateTo, Model model){
        LOGGER.debug("carStatistics between (dateFrom = {}, dateTo = {}), model = {}", dateFrom, dateTo, model);

        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        model.addAttribute("cars", carDtoService.findAllWithNumberOfOrders(dateFrom, dateTo));
        return "statistics";
    }

}

