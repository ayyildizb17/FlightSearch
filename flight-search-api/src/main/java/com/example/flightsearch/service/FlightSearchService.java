package com.example.flightsearch.service;

import com.example.flightsearch.mapper.FlightSearchMapper;
import com.example.flightsearch.repository.FlightSearchRepository;
import com.example.flightsearch.repository.dao.AirportEntity;
import com.example.flightsearch.repository.dao.FlightEntity;
import com.example.flightsearch.service.dto.AirportModel;
import com.example.flightsearch.service.dto.FlightModel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FlightSearchService {

    @Autowired
    private FlightSearchRepository flightSearchRepository;


    @Autowired
    private FlightSearchMapper flightSearchMapper;


    @Autowired
    private ResourceLoader resourceLoader;


    private List<FlightModel> getFlightData() throws IOException {
        List<FlightModel> flights = new ArrayList<>();
        Resource resource = resourceLoader.getResource("classpath:csvfiles/mock_data.csv");
        InputStreamReader reader = new InputStreamReader(resource.getInputStream());
        try (BufferedReader br = new BufferedReader(reader)) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                FlightModel flight = new FlightModel();
                flight.setDepartureDate(values[0]);
                flight.setReturnDate(values[1]);
                flight.setPrice(Double.parseDouble(values[2]));
                flight.setDepartureAirport(values[3]);
                flight.setArrivalAirport(values[4]);
                flights.add(flight);
            }
        }
        return flights;
    }

    @Scheduled(cron = "0 47 22 * * ?")
    public void syncFlightData() throws IOException {
        List<FlightModel> flightData = getFlightData();
        flightData.forEach(this::addNewFlight);

    }
    @Transactional
    public List<AirportModel> getAllAirports() {
        List<AirportEntity> airportEntityList = flightSearchRepository.getAllAirports();
        List<AirportModel> airportModelList = new ArrayList<>();
        airportEntityList.forEach(airportEntity -> {

            airportModelList.add(flightSearchMapper.mapAirportEntityToAirportModel(airportEntity));
        });
        return airportModelList;
    }

    @Transactional
    public List<FlightModel> getArrivingFlightsByCity(String city) {
        List<FlightEntity> flightEntityList = flightSearchRepository.getArrivingFlightListByCity(city);
        List<FlightModel> flightModels = new ArrayList<>();
        flightEntityList.forEach(flightEntity -> flightModels.add(flightSearchMapper
                .mapFlighEntityToFlightModel(flightEntity)));

        return flightModels;
    }

    @Transactional
    public List<FlightModel> getDepartingFlightsByCity(String city) {
        List<FlightEntity> flightEntityList = flightSearchRepository.getDepartingFlightListByCity(city);
        List<FlightModel> flightModels = new ArrayList<>();
        flightEntityList.forEach(flightEntity -> flightModels.add(flightSearchMapper
                .mapFlighEntityToFlightModel(flightEntity)));

        return flightModels;
    }

    public List<FlightModel> getAllFlights() {
        List<FlightEntity> flightEntityList = flightSearchRepository.getAllFlight();
        List<FlightModel> flightModels = new ArrayList<>();
        flightEntityList.forEach(flightEntity -> flightModels.add(flightSearchMapper
                .mapFlighEntityToFlightModel(flightEntity)));

        return flightModels;
    }

    public Boolean addNewAirport(AirportModel airportModel) {
        AirportEntity airportEntity = flightSearchMapper.mapAirportModelToAirportEntity(airportModel);
        return flightSearchRepository.addNewAirport(airportEntity);
    }

    public Boolean addNewFlight(FlightModel flightModel) {
        FlightEntity flightEntity = flightSearchMapper.mapFlightModelToFlightEntity(flightModel);
        if (flightEntity == null) {
            throw new RuntimeException();
        }
        return flightSearchRepository.addNewFlight(flightEntity);
    }

    public Boolean deleteAirport(String city) {
        return flightSearchRepository.deleteAirport(city);
    }

    public Boolean deleteFlight(FlightModel flightModel) {
        return flightSearchRepository.deleteFlight(flightModel);
    }

    public Boolean updateAirport(String city, AirportModel airportModel) {
        return flightSearchRepository.updateAirport(city, airportModel);
    }

    public Boolean updateFlight(FlightModel previousFlightModel, FlightModel updatedFlightModel) {
        return flightSearchRepository.updateFlight(previousFlightModel, updatedFlightModel);
    }

    public List<FlightModel> getFlights(FlightModel flightModel) throws Exception {
        List<FlightEntity> flightEntityList = new ArrayList<>();
        try {
            if (flightModel.getReturnDate() == null) {
                flightEntityList = flightSearchRepository.getAllFlight().stream().filter(a ->
                        a.getArrivalAirport().getCity().equals(flightModel.getArrivalAirport()) && a.getDepartureAirport().getCity().equals(flightModel.getDepartureAirport())
                                && flightSearchMapper.getStringFromDate(a.getDepartureDate()).equals(flightModel.getDepartureDate())
                                && a.getReturnDate() == null).collect(Collectors.toList());
            } else {
                flightEntityList = flightSearchRepository.getAllFlight().stream().filter(a ->
                        a.getArrivalAirport().getCity().equals(flightModel.getArrivalAirport()) && a.getDepartureAirport().getCity().equals(flightModel.getDepartureAirport())
                                && flightSearchMapper.getStringFromDate(a.getDepartureDate()).equals(flightModel.getDepartureDate())
                                && flightSearchMapper.getStringFromDate(a.getReturnDate()).equals(flightModel.getReturnDate())).collect(Collectors.toList());


            }


            return flightEntityList.stream().map(a -> flightSearchMapper.mapFlighEntityToFlightModel(a)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("There is no flight!!");
        }

    }
}
