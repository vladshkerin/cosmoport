package com.space.service;

import com.space.exception.BadRequestException;
import com.space.model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service("validationService")
public class ValidationService {

    private UtilsService utilsService;

    public boolean isShipNotValid(Ship ship, boolean checkEmptyField) {
        Field[] fields = ship.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }

            if (checkEmptyField) {
                try {
                    if (!"id isUsed rating".contains(field.getName()) && isEmpty(field.get(ship))) {
                        return true;
                    }
                } catch (IllegalAccessException e) {
                    throw new BadRequestException(e.getMessage());
                }
            }

            switch (field.getName()) {
                case "name":
                    if (ship.getName() != null) {
                        if (ship.getName().isEmpty() || ship.getName().length() >= 50) {
                            return true;
                        }
                    }
                    break;
                case "planet":
                    if (ship.getPlanet() != null) {
                        if (ship.getPlanet().isEmpty() || ship.getPlanet().length() >= 50) {
                            return true;
                        }
                    }
                    break;
                case "speed":
                    if (ship.getSpeed() != null) {
                        double speed = new BigDecimal(ship.getSpeed()).setScale(2, RoundingMode.UP).doubleValue();
                        if (speed < 0.01 || speed > 0.99) {
                            return true;
                        }
                    }
                case "crewSize":
                    if (ship.getCrewSize() != null) {
                        int crewSize = ship.getCrewSize();
                        if (crewSize < 1 || crewSize > 9999) {
                            return true;
                        }
                    }
                    break;
                case "prodDate":
                    if (ship.getProdDate() != null) {
                        int yearShip = utilsService.getYearShip(ship);
                        if (yearShip < 2800 || yearShip > 3019) {
                            return true;
                        }
                    }
            }
        }
        return false;
    }

    @Autowired
    public void setUtilsService(UtilsService utilsService) {
        this.utilsService = utilsService;
    }
}
