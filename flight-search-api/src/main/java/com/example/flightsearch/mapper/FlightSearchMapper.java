package com.example.flightsearch.mapper;

import com.example.flightsearch.repository.AirportRepository;
import com.example.flightsearch.repository.FlightRepository;
import com.example.flightsearch.repository.dao.AirportEntity;
import com.example.flightsearch.repository.dao.FlightEntity;
import com.example.flightsearch.service.dto.AirportModel;
import com.example.flightsearch.service.dto.FlightModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FlightSearchMapper {

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightRepository flightRepository;

    public FlightModel mapFlighEntityToFlightModel(FlightEntity flightEntity) {
        FlightModel flightModel = new FlightModel();
        flightModel.setPrice(flightEntity.getPrice());
        flightModel.setArrivalAirport(flightEntity.getArrivalAirport().getCity());
        flightModel.setDepartureAirport(flightEntity.getDepartureAirport().getCity());
        flightModel.setReturnDate(flightEntity.getReturnDate() != null ? getStringFromDate(flightEntity.getReturnDate()) : "");
        flightModel.setDepartureDate(getStringFromDate(flightEntity.getDepartureDate()));

        return flightModel;
    }

    public FlightEntity mapFlightModelToFlightEntity(FlightModel flightModel) {
        try {
            FlightEntity flightEntity = new FlightEntity();
            flightEntity.setPrice(flightModel.getPrice() != null ? flightModel.getPrice() : null);
            flightEntity.setArrivalAirport(airportRepository.findByCity(flightModel.getArrivalAirport()));
            flightEntity.setDepartureAirport(airportRepository.findByCity(flightModel.getDepartureAirport()));
            flightEntity.setReturnDate(flightModel.getReturnDate() != null ? getDate(flightModel.getReturnDate()) : null);
            flightEntity.setDepartureDate(flightModel.getDepartureDate() != null ? getDate(flightModel.getDepartureDate()) : null);
            return flightEntity;
        } catch (Exception e) {
            log.error("Exception occurred while converting flightModel to flightEntity",e);
            return null;
        }



    }

    public String getStringFromDate(Date date) {


        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateTimeString = dateTimeFormat.format(date);
        return dateTimeString;
    }

    public Date getDate(String date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date dateTime = dateTimeFormat.parse(date);
            return dateTime;

        } catch (ParseException e) {
            log.error("Date is not valid has to be formatted like yyyy-MM-dd HH:mm:ss !!",e);
            throw new RuntimeException();
        }

    }

    public AirportModel mapAirportEntityToAirportModel(AirportEntity airportEntity) {
        AirportModel airportModel = new AirportModel();
        airportModel.setCity(airportEntity.getCity());
        airportModel.setArrivingFlighList(airportEntity.getArrivingFlights().stream().map(this::mapFlighEntityToFlightModel).collect(Collectors.toList()));
        airportModel.setDepartingFlighList(airportEntity.getDepartingFlights().stream().map(this::mapFlighEntityToFlightModel).collect(Collectors.toList()));
        return airportModel;
    }

    public AirportEntity mapAirportModelToAirportEntity(AirportModel airportModel) {
        AirportEntity airportEntity = new AirportEntity();
        airportEntity.setCity(airportModel.getCity());
        if (airportModel.getArrivingFlighList() == null) {
            airportEntity.setArrivingFlights(new ArrayList<>());
        } else {
            airportEntity.setArrivingFlights(airportModel.getArrivingFlighList().stream().map(this::mapFlightModelToFlightEntity).collect(Collectors.toList()));

        }
        if(airportModel.getDepartingFlighList() == null) {
            airportEntity.setDepartingFlights(new ArrayList<>());
        } else {
            airportEntity.setDepartingFlights(airportModel.getDepartingFlighList().stream().map(this::mapFlightModelToFlightEntity).collect(Collectors.toList()));

        }
        return airportEntity;
    }


}
