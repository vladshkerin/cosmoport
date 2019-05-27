package com.space.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ship")
@NamedQueries({
        @NamedQuery(name = "Ship.findAll", query = "SELECT s FROM Ship s"),
        @NamedQuery(name = "Ship.findById", query = "SELECT distinct s FROM Ship s WHERE s.id = :id")
})
@SuppressWarnings("JpaQlInspection")
public class Ship implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "name")
    private String name;

    @Size(max = 50)
    @Column(name = "planet")
    private String planet;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipType")
    private ShipType shipType;

    @Temporal(TemporalType.DATE)
    @Column(name = "prodDate")
    private Date prodDate;

    @Column(name = "isUsed")
    private Boolean isUsed;

    @DecimalMin(value = "0.01")
    @DecimalMax(value = "0.99")
    @Column(name = "speed")
    private Double speed;

    @Min(1)
    @Max(9999)
    @Column(name = "crewSize")
    private Integer crewSize;

    @Column(name = "rating")
    private Double rating;
}
