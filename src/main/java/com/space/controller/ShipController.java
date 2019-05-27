package com.space.controller;

import com.google.common.collect.Lists;
import com.space.model.CountShip;
import com.space.model.Ship;
import com.space.service.ShipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/rest", produces = "application/json")
public class ShipController {

    @Autowired
    private ShipService jpaShipService;

    @GetMapping("/ships")
    public List<Ship> findAllByPage(@RequestParam Map<String, String> params) {
        String defaultSize = Integer.toString(jpaShipService.findAll().size());
        ShipOrder order = ShipOrder.valueOf(params.getOrDefault("order", "ID"));
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", defaultSize));

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        Page<Ship> shipPage = jpaShipService.findAllByPage(pageRequest);

        return Lists.newArrayList(shipPage.iterator());
    }

    @GetMapping("/ships/count")
    public CountShip countShips(@RequestParam Map<String, String> params) {
        log.info("CountShip ships");
        return new CountShip(findAllByPage(params).size());
    }

    @GetMapping("/ships/{id}")
    public Ship findById(@PathVariable Long id) {
        log.info("Show ship by id: " + id);
        return jpaShipService.findById(id);
    }

    @PostMapping("/ships")
    public Ship create(@RequestBody Ship ship) {
        log.info("Create ship");
        return jpaShipService.create(ship);
    }

    @PostMapping("/ships/{id}")
    public Ship update(@PathVariable Long id, @RequestBody Ship ship) {
        log.info("Update ship");
        return jpaShipService.update(ship);
    }

    @DeleteMapping("/ships/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete ship");
        jpaShipService.delete(id);
    }
}
