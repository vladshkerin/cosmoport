package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {

    List findAll();

    List<Ship> findAllWithDetail();

    int findCountAllWithDetail();

    Ship findById(Long id);

    Ship create();

    Ship save(Ship ship);

    void delete(Ship ship);

}
