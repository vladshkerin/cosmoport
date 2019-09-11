package com.space.model;

public enum ShipType {
    TRANSPORT("TRANSPORT"),
    MILITARY("MILITARY"),
    MERCHANT("MERCHANT"),
    EMPTY("");

    private final String shipType;

    ShipType(String shipType) {
        this.shipType = shipType;
    }

    @Override
    public String toString() {
        return this.shipType;
    }
}