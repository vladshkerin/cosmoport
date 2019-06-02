package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/rest", produces = "application/json")
public class ShipController {

    private ShipService shipService;

    @GetMapping("/ships")
    public List<Ship> findAllByPage(@RequestParam Map<String, String> params) {
        String name = params.getOrDefault("name", "");
        String planet = params.getOrDefault("planet", "");

        ShipOrder order = ShipOrder.valueOf(params.getOrDefault("order", "ID"));
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return shipService.findAllWithFilter(name, planet, pageable);
    }

    @GetMapping("/ships/count")
    public int countShips(@RequestParam Map<String, String> params) {
        log.info("CountShip ships");
        return findAllByPage(params).size();
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