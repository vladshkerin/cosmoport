package com.space.service.dao;

import com.space.model.Ship;

import java.util.List;
import java.util.Map;

public interface ShipService {

    List<Ship> findAllByCriteria(Map<String, String> params, boolean isPagination);

    Ship findById(Long id);

    Ship create(Ship ship);

    Ship update(Long id, Ship ship);

    void delete(Long ship);
}