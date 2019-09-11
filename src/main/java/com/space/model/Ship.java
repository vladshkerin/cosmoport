package com.space.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

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
}