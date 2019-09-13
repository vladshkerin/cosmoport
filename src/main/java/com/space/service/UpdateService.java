package com.space.service;

import com.space.model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("updateService")
public class UpdateService {

    private UtilsService utilsService;

    public void updateShip(Ship shipSrc, Ship shipDest) {
        if (shipSrc.getName() != null) {
            shipDest.setName(shipSrc.getName());
        }
        if (shipSrc.getPlanet() != null) {
            shipDest.setPlanet(shipSrc.getPlanet());
        }
        if (shipSrc.getShipType() != null) {
            shipDest.setShipType(shipSrc.getShipType());
        }
        if (shipSrc.getProdDate() != null) {
            shipDest.setProdDate(shipSrc.getProdDate());
        }
        if (shipSrc.getIsUsed() != null) {
            shipDest.setIsUsed(shipSrc.getIsUsed());
        }
        if (shipSrc.getSpeed() != null) {
            shipDest.setSpeed(shipSrc.getSpeed());
        }
        if (shipSrc.getCrewSize() != null) {
            shipDest.setCrewSize(shipSrc.getCrewSize());
        }
        if (shipSrc.getRating() != null) {
            shipDest.setRating(shipSrc.getRating());
        }
        shipDest.setRating(utilsService.calculateRating(shipDest));
    }

    @Autowired
    public void setUtilsService(UtilsService utilsService) {
        this.utilsService = utilsService;
    }
}
