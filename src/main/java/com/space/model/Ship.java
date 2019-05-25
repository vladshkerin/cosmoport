package com.space.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ship")
@NamedQueries({
        @NamedQuery(name="Ship.findAll", query="SELECT s FROM Ship s")
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

    @Column(name = "shipType")
    private String shipType;

    @Column(name = "prodDate")
    private Date prodDate;

    @Column(name = "isUsed")
    private Integer isUsed;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "crewSize")
    private Integer crewSize;

    @Column(name = "rating")
    private Double rating;

}
