package com.example.best_travel.infraestructure.helper;

import com.example.best_travel.domain.entities.jpa.*;
import com.example.best_travel.domain.repositories.jpa.ReservationRepository;
import com.example.best_travel.domain.repositories.jpa.TicketRepository;
import com.example.best_travel.infraestructure.service.ReservationService;
import com.example.best_travel.infraestructure.service.TicketService;
import com.example.best_travel.util.BestTravelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Transactional
@Component
@Slf4j
@AllArgsConstructor
public class TourHelper {

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public Set<TicketEntity> createTickets(Set<FlyEntity> flights, CustomerEntity customerEntity){

        var response = new HashSet<TicketEntity>(flights.size());
        flights.forEach(
                fly -> {
                    TicketEntity ticket = TicketEntity.builder()
                            .id(UUID.randomUUID())
                            .fly(fly)
                            .customer(customerEntity)
                            .price(fly.getPrice().add(fly.getPrice().multiply(TicketService.charger_price_percentage)))
                            .purchaseDate(LocalDate.now())
                            .departureDate(BestTravelUtil.getRandomSoon())
                            .arrivalDate(BestTravelUtil.getRandomSoon())
                            .build();

                response.add(ticketRepository.save(ticket));
                }
        );

        return response;

    }

    public Set<ReservationEntity> createReservations(HashMap<HotelEntity, Integer> hotels, CustomerEntity customerEntity){

        var response = new HashSet<ReservationEntity>(hotels.size());

        hotels.forEach((hotel, totalDays) -> {
            ReservationEntity reservationToPersist = ReservationEntity.builder()
                    .id(UUID.randomUUID())
                    .hotel(hotel)
                    .customer(customerEntity)
                    .totalDays(totalDays)
                    .dateTimeReservation(LocalDateTime.now())
                    .dateStart(LocalDate.now())
                    .dateEnd(LocalDate.now().plusDays(totalDays))
                    .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationService.charges_price_reservation)))
                    .build();
            response.add(reservationRepository.save(reservationToPersist));
        });
        return response;

    }

    public TicketEntity createTicket(FlyEntity fly, CustomerEntity customer){
        TicketEntity ticket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(TicketService.charger_price_percentage)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomSoon())
                .build();
        return ticketRepository.save(ticket);
    }

    public ReservationEntity createReservation(HotelEntity hotel, CustomerEntity customer, Integer totalDays){
        ReservationEntity reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(totalDays)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationService.charges_price_reservation)))
                .build();
        return this.reservationRepository.save(reservationToPersist);
    }

}
