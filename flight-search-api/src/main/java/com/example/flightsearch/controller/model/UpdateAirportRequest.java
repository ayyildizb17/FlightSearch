package com.example.flightsearch.controller.model;

import com.example.flightsearch.service.dto.AirportModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAirportRequest {

    private String previousAirportModel;
    private AirportModel updatedAirportModel;

}
