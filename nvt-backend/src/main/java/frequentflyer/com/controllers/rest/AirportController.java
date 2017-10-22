package frequentflyer.com.controllers.rest;

import frequentflyer.com.domain.AirportSearchDto;
import frequentflyer.com.services.AirportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sasaradovanovic on 10/21/17.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class AirportController {


    @Autowired
    private AirportService airportService;


    @RequestMapping(value = "airports", method = RequestMethod.GET)
    @ResponseBody
    public AirportSearchDto airportPartialSearch(@RequestParam String searchCriteria) {
        log.info("Airport search for " + searchCriteria);
        return airportService.partialSearch(searchCriteria);
    }

}