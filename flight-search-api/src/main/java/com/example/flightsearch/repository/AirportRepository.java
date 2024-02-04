package com.example.flightsearch.repository;

import com.example.flightsearch.repository.dao.AirportEntity;
import com.example.flightsearch.repository.dao.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<AirportEntity, Long> {


    AirportEntity findByCity(String city);

    @Query("SELECT f FROM FlightEntity f LEFT JOIN FETCH f.arrivalAirport WHERE f.arrivalAirport.city = :city")
    List<FlightEntity> getArrivingFlightListByCity(@Param("city") String city);

    @Query("SELECT f FROM FlightEntity f LEFT JOIN FETCH f.departureAirport WHERE f.departureAirport.city = :city")
    List<FlightEntity> getDepartingFlightListByCity(@Param("city") String city);
    void deleteByCity(String city);


}
