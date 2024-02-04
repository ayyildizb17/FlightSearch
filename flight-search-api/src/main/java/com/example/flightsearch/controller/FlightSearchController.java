package com.example.flightsearch.controller;

import com.example.flightsearch.controller.model.*;
import com.example.flightsearch.service.FlightSearchService;
import com.example.flightsearch.service.dto.AirportModel;
import com.example.flightsearch.service.dto.FlightModel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/flightSearch")
public class FlightSearchController {

    @Autowired
    private FlightSearchService flightSearchService;

    @GetMapping("/getAllFlights")
    private AllFlightsResponse getAllFlights() {
        AllFlightsResponse allFlightsResponse = new AllFlightsResponse();
        allFlightsResponse.setAllFlightsResponse(flightSearchService.getAllFlights());

        return allFlightsResponse;
    }


    @PostMapping("/addFlight")
    private Boolean addFlight(FlightModel flightModel) {
        try {
            return flightSearchService.addNewFlight(flightModel);
        } catch (Exception e) {
            return false;
        }

    }

    @PostMapping("/deleteFlight")
    private Boolean deleteFlight(FlightModel flightModel) {
        return flightSearchService.deleteFlight(flightModel);
    }

    @PostMapping("/updateFlight")
    private Boolean updateFlight(UpdateFlightRequest updateFlightRequest) {
        return flightSearchService.updateFlight(updateFlightRequest.getPreviousFlight(), updateFlightRequest.getUpdatedFlight());
    }

    @GetMapping("/getAllAirports")
    private AllAirportsResponse getAllAirports() {
        AllAirportsResponse allAirportsResponse = new AllAirportsResponse();
        allAirportsResponse.setAllAirportsResponse(flightSearchService.getAllAirports());
        return allAirportsResponse;
    }

    @GetMapping("/getAirportFlightList")
    private AirportFligtListResponse getAirportFlightList(String city) {
        AirportFligtListResponse airportFligtListResponse = new AirportFligtListResponse();
        airportFligtListResponse.setDepartingFlightList(flightSearchService.getDepartingFlightsByCity(city));
        airportFligtListResponse.setArrivingFlightList(flightSearchService.getArrivingFlightsByCity(city));
        return airportFligtListResponse;
    }

    @PostMapping("/addAirport")
    private Boolean addAirport(AddAirportRequest addAirportRequest) {
        AirportModel airportModel = new AirportModel();
        airportModel.setCity(addAirportRequest.getCity());
        airportModel.setArrivingFlighList(addAirportRequest.getArrivingFlightModelList());
        airportModel.setDepartingFlighList(addAirportRequest.getDepartingFlightModelList());
        return flightSearchService.addNewAirport(airportModel);
    }

    @PostMapping("/deleteAirport")
    private Boolean deleteAirport(String city) {
        return flightSearchService.deleteAirport(city);
    }

    @PostMapping("/updateAirport")
    private Boolean updateAirport(UpdateAirportRequest updateAirportRequest) {
        return flightSearchService.updateAirport(updateAirportRequest.getPreviousAirportModel(), updateAirportRequest.getUpdatedAirportModel());
    }

    @GetMapping("/searchFlight")
    private ResponseEntity<List<FlightModel>> getFlights(GetFlightRequest getFlightRequest) throws Exception {
        try {
            FlightModel flightModel = new FlightModel();
            flightModel.setDepartureAirport(getFlightRequest.getDepartureAirport());
            flightModel.setArrivalAirport(getFlightRequest.getArrivalAirport());
            flightModel.setDepartureDate(getFlightRequest.getDepartureDate());
            if (getFlightRequest.getReturnDate() != null) {
                flightModel.setReturnDate(getFlightRequest.getReturnDate());
            }
            return new ResponseEntity<>(flightSearchService.getFlights(flightModel), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
        }
    }




}
