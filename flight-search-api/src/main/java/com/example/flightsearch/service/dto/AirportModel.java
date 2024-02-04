package com.example.flightsearch.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AirportModel {
    private String city;
    private List<FlightModel> arrivingFlighList;
    private List<FlightModel> departingFlighList;
}
