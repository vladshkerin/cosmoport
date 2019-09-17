package com.space.service;

import com.space.model.Ship;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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
        Date date = ship.getProdDate();
        LocalDate localDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
        return localDate.getYear();
    }

    public boolean isStringNotLong(String s) {
        try {
            Long.parseLong(s);
            if (!s.contains(".")) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean isShipNotNew(Ship ship) {
        return !ship.equals(new Ship());
    }
}
