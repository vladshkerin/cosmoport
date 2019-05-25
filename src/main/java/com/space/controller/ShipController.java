package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/rest")
@Controller
public class ShipController {

    private final Logger logger = LoggerFactory.getLogger(ShipController.class);

    @Autowired
    private ShipService shipService;

    @RequestMapping(value = "/ships", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Ship> listShipRest() {
        logger.info("Listing ships");
        List<Ship> ships = shipService.findAll();
        return ships;
    }

}
