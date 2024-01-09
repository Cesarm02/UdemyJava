package com.example.best_travel.infraestructure.service;

import com.example.best_travel.api.models.request.TourRequest;
import com.example.best_travel.api.models.response.TourResponse;
import com.example.best_travel.domain.entities.*;
import com.example.best_travel.domain.repositories.CustomerRepository;
import com.example.best_travel.domain.repositories.FlyRepository;
import com.example.best_travel.domain.repositories.HotelRepository;
import com.example.best_travel.domain.repositories.TourRepository;
import com.example.best_travel.infraestructure.abstrat.ITourService;
import com.example.best_travel.infraestructure.helper.TourHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TourService implements ITourService {

    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final TourHelper tourHelper;


    @Override
    public TourResponse create(TourRequest request) {
        var customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow();
        var fligths = new HashSet<FlyEntity>();
        request.getFlights().forEach(
                fly -> fligths.add(
                        flyRepository.findById(fly.getId()).orElseThrow()
                )
        );
        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(
                hotel -> hotels.put(hotelRepository.findById(hotel.getId()).orElseThrow(),
                        hotel.getTotalDays())
        );

        var tourToSave = TourEntity.builder()
                .tickets(tourHelper.createTickets(fligths, customer))
                .reservations(tourHelper.createReservations(hotels, customer))
                .customer(customer)
                .build();

        var tourSaved = tourRepository.save(tourToSave);
        return TourResponse.builder()
                .reservationIds(tourSaved.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketsIds(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .id(tourSaved.getId())
                .build();
    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDb = tourRepository.findById(id).orElseThrow();
        return TourResponse.builder()
                .reservationIds(tourFromDb.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketsIds(tourFromDb.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .id(tourFromDb.getId())
                .build();
    }

    @Override
    public void delete(Long id) {
        var tourToDelete = tourRepository.findById(id).orElseThrow();
        tourRepository.delete(tourToDelete);
    }

    @Override
    public void removeTicket(Long tourId, UUID ticketId) {
        var tourUpdate = tourRepository.findById(tourId)
                .orElseThrow();
        tourUpdate.removeTicket(ticketId);
        tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addTicket(Long flyId, Long tourId) {
        var tourUpdate = tourRepository.findById(tourId)
                .orElseThrow();
        var fly = this.flyRepository.findById(flyId)
                .orElseThrow();
        var ticket = tourHelper.createTicket(fly, tourUpdate.getCustomer());
        tourUpdate.addTicket(ticket);
        tourRepository.save(tourUpdate);
        return ticket.getId();
    }

    @Override
    public void removeReservation(Long tourId, UUID reservationId) {

    }

    @Override
    public UUID addReservation(Long flyId, Long reservationId) {
        return null;
    }



}
