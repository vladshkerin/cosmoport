package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {

    List findAll();

    Ship findById(Long id);

    Ship create(Ship ship);

    Ship update(Ship ship);

    void delete(Long ship);

}
