package com.example.flightsearch.controller.model;

import com.example.flightsearch.service.dto.FlightModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AirportFligtListResponse {
    private List<FlightModel> departingFlightList;
    private List<FlightModel> arrivingFlightList;
}
