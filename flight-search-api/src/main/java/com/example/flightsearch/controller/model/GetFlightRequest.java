package com.example.flightsearch.controller.model;

import com.example.flightsearch.service.dto.FlightModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetFlightRequest {

    private String departureDate;
    private String returnDate;
    private String departureAirport;
    private String arrivalAirport;


}
