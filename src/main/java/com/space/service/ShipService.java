package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {

    List findAll();

    Ship findById(Long id);

    Ship save(Ship ship);

    void delete(Ship ship);

}
