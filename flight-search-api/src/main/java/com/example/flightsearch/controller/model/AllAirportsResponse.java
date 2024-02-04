package com.example.flightsearch.controller.model;

import com.example.flightsearch.service.dto.AirportModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllAirportsResponse {
    private List<AirportModel> allAirportsResponse;
}
