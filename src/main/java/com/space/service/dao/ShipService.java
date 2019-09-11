package com.space.service.dao;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ShipService {

    List<Ship> findAll();

    Page<Ship> findAllByPage(Pageable pageable);

    List<Ship> findAllByCriteria(Map<String, String> params);

    Ship findById(Long id);

    Ship create(Ship ship);

    Ship update(Ship ship);

    void delete(Long ship);
}