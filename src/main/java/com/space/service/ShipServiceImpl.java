package com.space.service;

import com.space.exception.BadRequestException;
import com.space.exception.NoFoundException;
import com.space.model.Ship;
import com.space.model.Ship_;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Ship> findAllByCriteria(Map<String, String> params) {
        String name = params.getOrDefault("name", "");
        String planet = params.getOrDefault("planet", "");
//        ShipType shipType = ShipType.valueOf(params.getOrDefault("shipType", "EMPTY"));
//        Long after = Long.valueOf(params.getOrDefault("after", "0"));
//        Long before = Long.valueOf(params.getOrDefault("before", "0"));
//        Boolean isUsed = Boolean.valueOf(params.getOrDefault("isUsed", "false"));
//        Double minSpeed = Double.valueOf(params.getOrDefault("minSpeed", "0.0"));
//        Double maxSpeed = Double.valueOf(params.getOrDefault("maxSpeed", "0.0"));
//        Integer minCrewSize = Integer.valueOf(params.getOrDefault("minCrewSize", "0"));
//        Integer maxCrewSize = Integer.valueOf(params.getOrDefault("maxCrewSize", "0"));
//        Double minRating = Double.valueOf(params.getOrDefault("minRating", "0.0"));
//        Double maxRating = Double.valueOf(params.getOrDefault("maxRating", "0.0"));
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ship> criteria = builder.createQuery(Ship.class);
        Root<Ship> root = criteria.from(Ship.class);
        criteria.select(root);

        Predicate predicate = builder.conjunction();
        if (!name.isEmpty()) {
            Predicate p = builder.like(root.get(Ship_.name), "%" + name + "%");
            predicate = builder.and(predicate, p);
        }
        if (!planet.isEmpty()) {
            Predicate p = builder.like(root.get(Ship_.planet), "%" + planet + "%");
            predicate = builder.and(predicate, p);
        }
        criteria.where(predicate);

        Query query = entityManager.createQuery(criteria);

        // Pagination
        if (pageNumber > 0) {
            int number = (pageSize == 0 ? 1 : pageSize) * pageNumber;
            query.setFirstResult(number);
        }
        if (pageSize > 0) {
            query.setMaxResults(pageSize);
        }

        List<Ship> result = query.getResultList();

        return result;
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
}