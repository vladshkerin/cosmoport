package com.space.service;

import com.space.model.Ship;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQueryApiInspection")
@Service("shipService")
@Repository
@Transactional
public class ShipServiceImpl implements ShipService {

    private final Log log = LogFactory.getLog(ShipServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<Ship> findAll() {
        return em.createNamedQuery("Ship.findAll", Ship.class).getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Ship findById(Long id) {
        TypedQuery<Ship> query = em.createNamedQuery("Ship.findById", Ship.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Ship save(Ship ship) {
        if (ship.getId() == null) {
            log.info("Inserting new ship");
            em.persist(ship);
        } else {
            log.info("Updating existing ship");
            em.merge(ship);
        }
        log.info("Ship saved with id: " + ship.getId());
        return ship;
    }

    @Override
    public void delete(Ship ship) {

    }
}
