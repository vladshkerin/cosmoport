package com.space.controller;

import com.space.model.Ship;
import com.space.service.dao.ShipServiceDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/rest", produces = "application/json")
@Slf4j
public class ShipController {

    private ShipServiceDAO shipServiceDAO;

    @GetMapping("/ships")
    public List<Ship> findAllWithCriteria(@RequestParam Map<String, String> params) {
        log.info("Find all ships with criteria");
        return shipServiceDAO.findAllByCriteria(params, true);
    }

    @GetMapping("/ships/count")
    public int countShips(@RequestParam Map<String, String> params) {
        log.info("Count ships");
        return shipServiceDAO.findAllByCriteria(params, false).size();
    }

    @GetMapping("/ships/{id}")
    public Ship findById(@PathVariable Long id) {
        log.info("Find ship by id: " + id);
        return shipServiceDAO.findById(id);
    }

    @PostMapping("/ships")
    public Ship create(@RequestBody Ship ship) {
        log.info("Create ship");
        return shipServiceDAO.create(ship);
    }

    @PostMapping("/ships/{id}")
    public Ship update(@PathVariable Long id, @RequestBody Ship ship) {
        log.info("Update ship");
        return shipServiceDAO.update(id, ship);
    }

    @DeleteMapping("/ships/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete ship");
        shipServiceDAO.delete(id);
    }

    @Autowired
    public void setShipServiceDAO(ShipServiceDAO shipServiceDAO) {
        this.shipServiceDAO = shipServiceDAO;
    }
}