package com.space.service;

import com.space.exception.BadRequestException;
import com.space.exception.NoFoundException;
import com.space.model.Ship;
import com.space.repository.ShipJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Repository
@Transactional
@Service("shipService")
public class ShipServiceImpl implements ShipService {

    @PersistenceContext
    private EntityManager em;

    private ShipJpaRepository shipJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Ship> findAllByPage(Pageable pageable) {
        return shipJpaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ship> findAllWithFilter(String name, String planet, Pageable pageable) {
        return shipJpaRepository.findAllWithFilter(name, planet, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ship> findAllByNameContainingAndPlanetContaining(String name, String planet, Pageable pageable) {
        return shipJpaRepository.findAllByNameContainingAndPlanetContaining(name, planet, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Ship findById(Long id) {
        if (!isStringLong(String.valueOf(id)) || id < 1) {
            throw new BadRequestException("Not validation id: " + id);
        }

        Optional<Ship> shipOptional = shipJpaRepository.findById(id);
        if (shipOptional.isPresent()) {
            return shipOptional.get();
        } else {
            throw new NoFoundException("Not found ship with id: " + id);
        }
    }

    @Override
    public Ship create(Ship ship) {
        log.info("Inserting new ship");
        if (validationShip(ship)) {
            ship.setRating(calculateRating(ship, 1));
            em.persist(ship);
        } else {
            throw new BadRequestException("Validate error with id: " + ship.getId());
        }
        return ship;
    }

    @Override
    public Ship update(Ship ship) {
        log.info("Updating existing ship");
        em.merge(ship);
        return ship;
    }

    @Override
    public void delete(Long id) {
        Ship ship = findById(id);
        Ship mergedShip = em.merge(ship);
        em.remove(mergedShip);
        log.info("Deleted ship with id: " + id);
    }

    private boolean isStringLong(String s) {
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

    private boolean validationShip(Ship ship) {
        Field[] fields = ship.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }

            switch (field.getName()) {
                case "name":
                case "planet":
                case "shipType":
                case "speed":
                case "crewSize":
                    try {
                        if (isEmpty(field.get(ship))) {
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        throw new BadRequestException(e.getMessage());
                    }
                    break;
                case "prodDate":
                    int yearShip = getYearShip(ship);
                    if (yearShip < 2800 || yearShip > 3019) {
                        return false;
                    }
            }
        }
        return true;
    }

    private double calculateRating(Ship ship, Integer ratio) {
        double rating = (80.0 * ship.getSpeed() * ratio) / (3019.0 - getYearShip(ship) + 1);
        return BigDecimal.valueOf(rating)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private int getYearShip(Ship ship) {
        LocalDate localDate = ship.getProdDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear();
    }

    @Autowired
    public void setShipJpaRepository(ShipJpaRepository shipJpaRepository) {
        this.shipJpaRepository = shipJpaRepository;
    }
}