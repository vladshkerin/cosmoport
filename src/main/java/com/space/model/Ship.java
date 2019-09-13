package com.space.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "ship")
public class Ship implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Size(max = 50)
    @Column
    private String name;

    @Size(max = 50)
    @Column
    private String planet;

    @Enumerated(EnumType.STRING)
    @Column
    private ShipType shipType;

    @Temporal(TemporalType.DATE)
    @Column
    private Date prodDate;

    @Column
    private Boolean isUsed = false;

    @DecimalMin(value = "0.01")
    @DecimalMax(value = "0.99")
    @Column
    private Double speed;

    @Min(1)
    @Max(9999)
    @Column
    private Integer crewSize;

    @Column
    private Double rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return Objects.equals(id, ship.id) &&
                Objects.equals(name, ship.name) &&
                Objects.equals(planet, ship.planet) &&
                shipType == ship.shipType &&
                Objects.equals(prodDate, ship.prodDate) &&
                Objects.equals(isUsed, ship.isUsed) &&
                Objects.equals(speed, ship.speed) &&
                Objects.equals(crewSize, ship.crewSize) &&
                Objects.equals(rating, ship.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, planet, shipType, prodDate, isUsed, speed, crewSize, rating);
    }
}