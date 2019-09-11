package com.space.service;

import com.space.model.Ship;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;

@Service("utilsService")
public class UtilsService {

    public double calculateRating(Ship ship) {
        double ratio = ship.getIsUsed() ? 0.5 : 1;
        double rating = (80.0 * ship.getSpeed() * ratio) / (3019.0 - getYearShip(ship) + 1);
        return BigDecimal.valueOf(rating)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    int getYearShip(Ship ship) {
        LocalDate localDate = ship.getProdDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear();
    }

    public boolean isStringLong(String s) {
        try {
            Long.parseLong(s);
            if (!s.contains(".")) {
                return true;
            }
        } catch (NumberFormatException e) {
            //empty body
        }
        return false;
    }

}
