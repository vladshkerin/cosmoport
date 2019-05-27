package com.space.service;

import com.space.exception.ValidationShipException;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Repository
@Transactional
@Service("jpaShipService")
@SuppressWarnings("JpaQueryApiInspection")
public class ShipServiceImpl implements ShipService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ShipRepository shipRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Ship> findAll() {
        return em.createNamedQuery("Ship.findAll", Ship.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ship> findAllByPage(Pageable pageable) {
        return shipRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Ship findById(Long id) {
        TypedQuery<Ship> query = em.createNamedQuery("Ship.findById", Ship.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Ship create(Ship ship) {
        log.info("Inserting new ship");
        if (validationShip(ship)) {
            ship.setRating(calculateRating(ship, 1));
            em.persist(ship);
        } else {
            throw new ValidationShipException("Validate error with id: " + ship.getId());
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
        if (ship != null) {
            Ship mergedShip = em.merge(ship);
            em.remove(mergedShip);
            log.info("Deleted ship with id: " + id);
        } else {
            //
        }
    }

    private boolean validationShip(Ship ship) {
        Field[] fields = ship.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (!"id".equals(fieldName) &&
                        !"isUsed".equals(fieldName) &&
                        !"rating".equals(fieldName)) {
                    if ("prodDate".equals(fieldName)) {
                        int yearShip = getYearShip(ship);
                        if (yearShip < 2800 || yearShip > 3019) {
                            return false;
                        }
                    } else {
                        try {
                            if (isEmpty(field.get(ship))) {
                                return false;
                            }
                        } catch (IllegalAccessException e) {
                            throw new ValidationShipException(e.getMessage());
                        }
                    }
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
}
