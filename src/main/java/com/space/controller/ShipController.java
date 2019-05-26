package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest",  produces = "application/json")
public class ShipController {

    private final Log log = LogFactory.getLog(ShipController.class);

    private ShipService shipService;

    @GetMapping("/ships")
    public List findAll() {
        log.info("Listing ships");
        return shipService.findAll();
    }

    @GetMapping("/ships/id")
    public Ship findById(Long id) {
        log.info("Show ship by id: " + id);
        return shipService.findById(id);
    }

    @PostMapping("/ships")
    @ResponseStatus(HttpStatus.CREATED)
    public Ship save(@RequestBody Ship ship) {
        log.info("Save ship");
        return shipService.save(ship);
    }

    @GetMapping("/ships/count")
    public int countShips() {
        log.info("Count ships");
        return shipService.findAll().size();
    }

    @Autowired
    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }
}
