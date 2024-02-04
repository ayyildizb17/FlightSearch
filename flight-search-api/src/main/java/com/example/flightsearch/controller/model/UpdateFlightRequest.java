package com.example.flightsearch.controller.model;

import com.example.flightsearch.service.dto.FlightModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFlightRequest {

    private FlightModel previousFlight;
    private FlightModel updatedFlight;

}
