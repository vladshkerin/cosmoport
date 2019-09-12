package com.space.controller;

import com.space.model.Ship;
import com.space.service.dao.ShipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/rest", produces = "application/json")
@Slf4j
public class ShipController {

    private ShipService shipService;

    @GetMapping("/ships")
    public List<Ship> findAllWithCriteria(@RequestParam Map<String, String> params) {
        log.info("Find all ship with criteria");
        return shipService.findAllByCriteria(params, true);
    }

    @GetMapping("/ships/count")
    public int countShips(@RequestParam Map<String, String> params) {
        log.info("CountShip ships");
        return shipService.findAllByCriteria(params, false).size();
    }

    @GetMapping("/ships/{id}")
    public Ship findById(@PathVariable Long id) {
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

    @Autowired
    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }
}