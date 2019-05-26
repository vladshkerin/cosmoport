package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/rest", produces = "application/json")
public class ShipController {

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
    public Ship create(@RequestBody Ship ship) {
        log.info("Create ship");
        return shipService.create(ship);
    }

    @PostMapping("/ships/{id}")
    public Ship update(@PathVariable Long id, @RequestBody Ship ship) {
        log.info("Update ship");
        return shipService.update(ship);
    }

    @DeleteMapping("/ships/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete ship");
        shipService.delete(id);
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
