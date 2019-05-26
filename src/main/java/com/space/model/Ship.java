package com.space.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("JpaQlInspection")
@Getter
@Setter
@Entity
@Table(name = "ship")
@NamedQueries({
        @NamedQuery(name = "Ship.findAll", query = "SELECT s FROM Ship s"),
        @NamedQuery(name = "Ship.findById", query = "SELECT distinct s FROM Ship s WHERE s.id = :id")
})
public class Ship implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

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

    @Column(name = "speed")
    private Double speed;

    @Column(name = "crewSize")
    private Integer crewSize;

    @Column(name = "rating")
    private Double rating;

}
