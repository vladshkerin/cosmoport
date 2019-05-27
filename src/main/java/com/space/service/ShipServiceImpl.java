package com.space.service;

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
import java.util.List;

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
        em.persist(ship);
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
}
