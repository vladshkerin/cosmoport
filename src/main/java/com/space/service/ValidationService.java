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

    public boolean validationShip(Ship ship) {
        Field[] fields = ship.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }

            try {
                if (!"id isUsed rating".contains(field.getName()) && isEmpty(field.get(ship))) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new BadRequestException(e.getMessage());
            }

            switch (field.getName()) {
                case "name":
                    if (ship.getName().length() >= 50) {
                        return false;
                    }
                    break;
                case "planet":
                    if (ship.getPlanet().length() >= 50) {
                        return false;
                    }
                    break;
                case "speed":
                    double speed = new BigDecimal(ship.getSpeed()).setScale(2, RoundingMode.UP).doubleValue();
                    if (speed < 0.01 || speed > 0.99) {
                        return false;
                    }
                case "crewSize":
                    int crewSize = ship.getCrewSize();
                    if (crewSize < 1 || crewSize > 9999) {
                        return false;
                    }
                    break;
                case "prodDate":
                    int yearShip = utilsService.getYearShip(ship);
                    if (yearShip < 2800 || yearShip > 3019) {
                        return false;
                    }
            }
        }
        return true;
    }

    @Autowired
    public void setUtilsService(UtilsService utilsService) {
        this.utilsService = utilsService;
    }
}
