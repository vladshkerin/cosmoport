package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipService {

    List findAll();

    Page<Ship> findAllByPage(Pageable pageable);

    Ship findById(Long id);

    Ship create(Ship ship);

    Ship update(Ship ship);

    void delete(Long ship);

}
