package com.space.model;

import lombok.Data;

@Data
public class CountShip {

    private Integer count;

    public CountShip(Integer count) {
        this.count = count;
    }
}
