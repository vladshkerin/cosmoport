package com.space.service.dao;

import com.space.exception.BadRequestException;
import com.space.exception.NoFoundException;
import com.space.model.Ship;
import com.space.service.CriteriaService;
import com.space.service.UtilsService;
import com.space.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Service("shipService")
@Repository
@Transactional
@Slf4j
public class ShipServiceImpl implements ShipService {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaService criteriaService;

    private ValidationService validationService;

    private UtilsService utilsService;

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public List<Ship> findAllByCriteria(Map<String, String> params, boolean isPagination) {
        Query query = criteriaService.createQuery(entityManager, params, isPagination);
        return (List<Ship>) query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Ship findById(Long id) {
        if (!utilsService.isStringLong(String.valueOf(id)) || id < 1) {
            throw new BadRequestException("Not validation id: " + id);
        }

        Ship ship = entityManager.find(Ship.class, id);
        if (ship == null) {
            throw new NoFoundException("Not found ship with id: " + id);
        }

        return ship;
    }

    @Override
    public Ship create(Ship ship) {
        if (!validationService.validationShip(ship, true)) {
            throw new BadRequestException("Validate error with id: " + ship.getId());
        }

        ship.setRating(utilsService.calculateRating(ship));
        entityManager.persist(ship);

        log.info("Ship saved with id: " + ship.getId());
        return ship;
    }

    @Override
    public Ship update(Long id, Ship shipSrc) {
        if (!utilsService.isStringLong(String.valueOf(id)) || id < 1) {
            throw new BadRequestException("Not validation id: " + id);
        } else if (!shipSrc.equals(new Ship()) && !validationService.validationShip(shipSrc, false)) {
            throw new BadRequestException("Validate error with id: " + shipSrc.getId());
        }

        Ship shipDest = entityManager.find(Ship.class, id);
        if (shipDest == null) {
            throw new NoFoundException("Not found ship with id: " + id);
        }

        if (!shipSrc.equals(new Ship())) {
            updateShip(shipSrc, shipDest);
            entityManager.merge(shipDest);
        }

        log.info("Ship updated with id: " + shipDest.getId());

        return shipDest;
    }

    private void updateShip(Ship shipSrc, Ship shipDest) {
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

    @Override
    public void delete(Long id) {
        Ship ship = findById(id);
        Ship mergedShip = entityManager.merge(ship);
        entityManager.remove(mergedShip);
        log.info("Ship deleted with id: " + id);
    }

    @Autowired
    public void setCriteriaService(CriteriaService criteriaService) {
        this.criteriaService = criteriaService;
    }

    @Autowired
    public void setValidationService(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Autowired
    public void setUtilsService(UtilsService utilsService) {
        this.utilsService = utilsService;
    }
}