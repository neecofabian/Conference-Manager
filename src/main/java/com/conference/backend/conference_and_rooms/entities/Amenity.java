package com.conference.backend.conference_and_rooms.entities;

/**
 * The possible Amenities of a Room
 */
public enum Amenity {
    AV_EQUIPMENT("Audio/Visual Equipment"),
    PODIUM("Podium"),
    TABLES("Tables"),
    CHAIRS("Chairs"),
    WIFI("Wifi"),
    RESTROOMS("Restrooms");

    private String amenity;

    Amenity(String amenity) {
        this.amenity = amenity;
    }

    /**
     * Return a String representation of the Amenity
     *
     * @return a String representation of the Amenity
     */
    @Override
    public String toString(){
        return amenity;
    }
}
