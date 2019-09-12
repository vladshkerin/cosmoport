package com.space.service.dao;

import com.space.exception.BadRequestException;
import com.space.exception.NoFoundException;
import com.space.model.Ship;
import com.space.repository.ShipJpaRepository;
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
import java.util.Optional;

@Service("shipService")
@Repository
@Transactional
@Slf4j
public class ShipServiceImpl implements ShipService {

    @PersistenceContext
    private EntityManager entityManager;

    private ShipJpaRepository shipJpaRepository;

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

        Optional<Ship> shipOptional = shipJpaRepository.findById(id);
        if (shipOptional.isPresent()) {
            return shipOptional.get();
        } else {
            throw new NoFoundException("Not found ship with id: " + id);
        }
    }

    @Override
    public Ship create(Ship ship) {
        if (validationService.validationShip(ship)) {
            ship.setRating(utilsService.calculateRating(ship));
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

    @Autowired
    public void setShipJpaRepository(ShipJpaRepository shipJpaRepository) {
        this.shipJpaRepository = shipJpaRepository;
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