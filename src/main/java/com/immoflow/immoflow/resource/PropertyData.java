package com.immoflow.immoflow.resource;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "property")
@Data
@ToString
public class PropertyData {

    //todo ggf nachimplemntieren
    // - Energieträger
    // - Objektzustand

    private String       id;
    private String       title;
    private String       country = "Deutschland";
    private String       city;
    private String       district;
    private String       street;
    private String       zipCode;
    private String       costsCold;
    private String       costsAdditional;
    private String       costsHeating;
    private String       costsTotal;
    private String       costsParkingSpace;
    private String       kaution;
    private String       rooms;
    private String       roomsBath;
    private ParkingSpace parkingSpace;
    private String       moveInDate;
    private String       constructionYear;
    private String       lastRenovation;
    private String       areaInM2;
    private String       propertyType;
    private String       floor;
    private String       petsAllowed;
    private Keller       keller;
    private String       httpLink;

}
