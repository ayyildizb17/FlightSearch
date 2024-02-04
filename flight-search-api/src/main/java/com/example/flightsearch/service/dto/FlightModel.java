package com.example.flightsearch.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlightModel {


    private String departureDate;

    private String returnDate;

    private Double price;

    private String departureAirport;
    private String arrivalAirport;
}
