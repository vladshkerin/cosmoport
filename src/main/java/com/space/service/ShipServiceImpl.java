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
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service("shipService")
@Repository
@Transactional
@Slf4j
public class ShipServiceImpl implements ShipService {

    @PersistenceContext
    private EntityManager entityManager;

    private ShipJpaRepository shipJpaRepository;

    private CriteriaService criteriaService;

    @Transactional(readOnly = true)
    @Override
    public List<Ship> findAll() {
        return shipJpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Ship> findAllByPage(Pageable pageable) {
        return shipJpaRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public List<Ship> findAllByCriteria(Map<String, String> params) {
        Query query = criteriaService.createQuery(entityManager, params);
        return (List<Ship>) query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
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
        if (validationShip(ship)) {
            ship.setRating(calculateRating(ship));
            entityManager.persist(ship);
        } else {
            throw new BadRequestException("Validate error with id: " + ship.getId());
        }
        log.info("Ship saved with id: " + ship.getId());
        return ship;
    }

    @Override
    public Ship update(Ship ship) {
        entityManager.merge(ship);
        log.info("Ship updated with id: " + ship.getId());
        return ship;
    }

    @Override
    public void delete(Long id) {
        Ship ship = findById(id);
        Ship mergedShip = entityManager.merge(ship);
        entityManager.remove(mergedShip);
        log.info("Ship deleted with id: " + id);
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
                    int yearShip = getYearShip(ship);
                    if (yearShip < 2800 || yearShip > 3019) {
                        return false;
                    }
            }
        }
        return true;
    }

    private double calculateRating(Ship ship) {
        double ratio = ship.getIsUsed() ? 0.5 : 1;
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

    @Autowired
    public void setCriteriaService(CriteriaService criteriaService) {
        this.criteriaService = criteriaService;
    }
}