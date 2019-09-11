package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("shipJpaRepository")
public interface ShipJpaRepository extends JpaRepository<Ship, Long>, PagingAndSortingRepository<Ship, Long> {

    @Query("SELECT s FROM Ship s WHERE s.name LIKE %:name% AND s.planet LIKE %:planet%")
    List<Ship> findAllWithFilter(@Param("name") String name,
                                 @Param("planet") String planet,
                                 Pageable pageable);
}
