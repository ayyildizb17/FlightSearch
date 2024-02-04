package com.example.flightsearch.repository;

import com.example.flightsearch.mapper.FlightSearchMapper;
import com.example.flightsearch.repository.dao.AirportEntity;
import com.example.flightsearch.repository.dao.FlightEntity;
import com.example.flightsearch.service.dto.AirportModel;
import com.example.flightsearch.service.dto.FlightModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FlightSearchRepository {

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightSearchMapper flightSearchMapper;

    public List<AirportEntity> getAllAirports() {
        return airportRepository.findAll();
    }

    public List<FlightEntity> getArrivingFlightListByCity(String city) {
        return airportRepository.getArrivingFlightListByCity(city);
    }



    public List<FlightEntity> getDepartingFlightListByCity(String city) {
        return airportRepository.getDepartingFlightListByCity(city);
    }

    public List<FlightEntity> getAllFlight() {
        return flightRepository.findAll();
    }

    public Boolean addNewAirport(AirportEntity airportEntity) {
        try {
            airportRepository.save(airportEntity);
            return true;
        } catch (Exception e) {
            log.error("Exception occurred while saving airportEntity to db: ",e);
            return false;
        }
    }

    public Boolean addNewFlight(FlightEntity flightEntity) {
        try {
            List<FlightEntity> flightEntityList = flightRepository.findAll();
            if (flightEntityList.contains(flightEntity)) {
                throw new Exception();
            }
            flightRepository.save(flightEntity);
            return true;
        } catch (Exception e) {
            log.error("Exception occurred while saving flightEntity to db: ",e);
            return false;
        }
    }

    @Transactional
    public Boolean deleteAirport(String city) {
        try {
            airportRepository.deleteByCity(city);
            return true;
        } catch (Exception e) {
            log.error("Exception occurred while deleting airportEntity from db: ",e);
            return false;
        }
    }

    @Transactional
    public Boolean deleteFlight(FlightModel flightModel) {
        try {
            FlightEntity flightEntity = flightSearchMapper.mapFlightModelToFlightEntity(flightModel);
            flightRepository.delete(flightEntity);
            return true;
        } catch (Exception e) {
            log.error("Exception occurred while deleting flightEntity from db: ",e);
            return false;
        }
    }

    public Boolean updateAirport(String previousAirport,AirportModel updatedAirportModel) {
        try {
            List<FlightEntity> arrivingFlightEntityList = new ArrayList<>();
            List<FlightEntity> departingFlightEntityList = new ArrayList<>();
            AirportEntity airportEntity = airportRepository.findByCity(previousAirport);

            airportEntity.setCity(updatedAirportModel.getCity());
            if (updatedAirportModel.getDepartingFlighList() != null) {
                updatedAirportModel.getDepartingFlighList().stream()
                        .map(flightModel -> departingFlightEntityList.add(flightSearchMapper.mapFlightModelToFlightEntity(flightModel)));
                airportEntity.setDepartingFlights(departingFlightEntityList);
            }
            if (updatedAirportModel.getArrivingFlighList() != null) {
                updatedAirportModel.getArrivingFlighList().stream()
                        .map(flightModel -> arrivingFlightEntityList.add(flightSearchMapper.mapFlightModelToFlightEntity(flightModel)));
                airportEntity.setArrivingFlights(arrivingFlightEntityList);
            }
            airportRepository.save(airportEntity);
            return true;
        } catch (Exception e){
            log.error("Exception occured while updating airport");
            return false;
        }
    }

    public Boolean updateFlight(FlightModel previousFlightModel,FlightModel updatedFlightModel) {
        try {
            Optional<FlightEntity> optionalFlightEntity = flightRepository.findAll()
                    .stream()
                    .filter(a -> {
                        FlightEntity mappedEntity = flightSearchMapper.mapFlightModelToFlightEntity(previousFlightModel);


                        return flightSearchMapper.getDate(flightSearchMapper.getStringFromDate(a.getDepartureDate())).equals(mappedEntity.getDepartureDate())
                                && a.getArrivalAirport().equals(mappedEntity.getArrivalAirport())
                                && a.getDepartureAirport().equals(mappedEntity.getDepartureAirport());
                    })
                    .collect(Collectors.toList()).stream().findFirst();
            FlightEntity flightEntity = optionalFlightEntity.orElse(null);
            if (flightEntity == null) {
                throw new Exception("Provided previousFlightModel is not valid!!");
            }
            FlightEntity updatedFlightEntity = flightSearchMapper.mapFlightModelToFlightEntity(updatedFlightModel);
            flightEntity.setPrice(updatedFlightEntity.getPrice() != null ? updatedFlightEntity.getPrice() : flightEntity.getPrice());
            flightEntity.setReturnDate(updatedFlightEntity.getReturnDate() != null ? updatedFlightEntity.getReturnDate() : flightEntity.getReturnDate());
            flightEntity.setArrivalAirport(updatedFlightEntity.getArrivalAirport() != null ? updatedFlightEntity.getArrivalAirport() : flightEntity.getArrivalAirport());
            flightEntity.setDepartureAirport(updatedFlightEntity.getDepartureAirport() != null ? updatedFlightEntity.getDepartureAirport() : flightEntity.getDepartureAirport());
            flightEntity.setDepartureDate(updatedFlightEntity.getDepartureDate() != null ? updatedFlightEntity.getDepartureDate() : flightEntity.getDepartureDate());
            flightRepository.save(flightEntity);
            return true;
        } catch (Exception e){
            log.error("Exception occured while updating airport: ",e);
            return false;
        }
    }




}
