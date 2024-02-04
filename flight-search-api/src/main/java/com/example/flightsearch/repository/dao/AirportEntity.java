package com.example.flightsearch.repository.dao;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Table(name = "airport")
@Entity
public class AirportEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "city", unique = true)
    private String city;

    @OneToMany(mappedBy = "departureAirport", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FlightEntity> departingFlights;

    @OneToMany(mappedBy = "arrivalAirport", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FlightEntity> arrivingFlights;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirportEntity that = (AirportEntity) o;
        return Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city);
    }


}
