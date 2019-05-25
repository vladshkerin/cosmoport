package com.space.service;

import com.space.model.Ship;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
@Service("shipService")
public class ShipServiceImpl implements ShipService {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("JpaQueryApiInspection")
    @Transactional(readOnly = true)
    @Override
    public List<Ship> findAll() {
        return em.createNamedQuery("Ship.findAll", Ship.class).getResultList();
    }

    @Override
    public List<Ship> findAllWithDetail() {
        return null;
    }

    @Override
    public int findCountAllWithDetail() {
        return 0;
    }

    @Override
    public Ship findById(Long id) {
        return null;
    }

    @Override
    public Ship create() {
        return null;
    }

    @Override
    public Ship save(Ship ship) {
        return null;
    }

    @Override
    public void delete(Ship ship) {

    }

}
