package com.example.best_travel.infraestructure.service;

import com.example.best_travel.api.models.request.TourRequest;
import com.example.best_travel.api.models.response.TourResponse;
import com.example.best_travel.domain.entities.jpa.*;
import com.example.best_travel.domain.repositories.jpa.CustomerRepository;
import com.example.best_travel.domain.repositories.jpa.FlyRepository;
import com.example.best_travel.domain.repositories.jpa.HotelRepository;
import com.example.best_travel.domain.repositories.jpa.TourRepository;
import com.example.best_travel.infraestructure.abstrat.ITourService;
import com.example.best_travel.infraestructure.helper.BlackListHelper;
import com.example.best_travel.infraestructure.helper.CustomerHelper;
import com.example.best_travel.infraestructure.helper.EmailHelper;
import com.example.best_travel.infraestructure.helper.TourHelper;
import com.example.best_travel.util.enums.Tables;
import com.example.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
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

    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;
    @Override
    public TourResponse create(TourRequest request) {

        blackListHelper.isInBlackListCustomer(request.getCustomerId());

        var customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow( () -> new IdNotFoundException(Tables.customer.name()));
        var fligths = new HashSet<FlyEntity>();
        request.getFlights().forEach(
                fly -> fligths.add(
                        flyRepository.findById(fly.getId()).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()))
                )
        );
        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(
                hotel -> hotels.put(hotelRepository.findById(hotel.getId()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name())),
                        hotel.getTotalDays())
        );

        var tourToSave = TourEntity.builder()
                .tickets(tourHelper.createTickets(fligths, customer))
                .reservations(tourHelper.createReservations(hotels, customer))
                .customer(customer)
                .build();

        var tourSaved = tourRepository.save(tourToSave);

        this.customerHelper.incrase(customer.getDni(), TourService.class);


        if(Objects.nonNull(request.getEmail()))
            this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.tour.name());

        return TourResponse.builder()
                .reservationIds(tourSaved.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketsIds(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .id(tourSaved.getId())
                .build();
    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDb = tourRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        return TourResponse.builder()
                .reservationIds(tourFromDb.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketsIds(tourFromDb.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .id(tourFromDb.getId())
                .build();
    }

    @Override
    public void delete(Long id) {
        var tourToDelete = tourRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        tourRepository.delete(tourToDelete);
    }

    @Override
    public void removeTicket(Long tourId, UUID ticketId) {
        var tourUpdate = tourRepository.findById(tourId)
                .orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeTicket(ticketId);
        tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addTicket(Long flyId, Long tourId) {
        var tourUpdate = tourRepository.findById(tourId)
                .orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        var fly = this.flyRepository.findById(flyId)
                .orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
        var ticket = tourHelper.createTicket(fly, tourUpdate.getCustomer());
        tourUpdate.addTicket(ticket);
        tourRepository.save(tourUpdate);
        return ticket.getId();
    }

    @Override
    public void removeReservation(Long tourId, UUID reservationId) {
        var tourUpdate = tourRepository.findById(tourId)
                .orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeReservation(reservationId);
        tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addReservation(Long flyId, Long reservationId, Integer totalDays) {
        var tourUpdate = tourRepository.findById(reservationId)
                .orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        var hotel = this.hotelRepository.findById(flyId)
                .orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
        var reservation = tourHelper.createReservation(hotel, tourUpdate.getCustomer(), totalDays);
        tourUpdate.addReservation(reservation);
        tourRepository.save(tourUpdate);
        return reservation.getId();
    }



}
