package com.example.best_travel.infraestructure.service;

import com.example.best_travel.api.models.request.ReservationRequest;
import com.example.best_travel.api.models.response.HotelResponse;
import com.example.best_travel.api.models.response.ReservationResponse;
import com.example.best_travel.domain.entities.CustomerEntity;
import com.example.best_travel.domain.entities.HotelEntity;
import com.example.best_travel.domain.entities.ReservationEntity;
import com.example.best_travel.domain.repositories.CustomerRepository;
import com.example.best_travel.domain.repositories.HotelRepository;
import com.example.best_travel.domain.repositories.ReservationRepository;
import com.example.best_travel.infraestructure.abstrat.IReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {

    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        HotelEntity hotel = hotelRepository.findById(request.getIdHotel())
                .orElseThrow();
        CustomerEntity customer = customerRepository.findById(request.getIdClient())
                .orElseThrow();
        ReservationEntity reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(request.getTotalDays())
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_reservation)))
                .build();

        ReservationEntity reservtionPersist = reservationRepository.save(reservationToPersist);

        return entityToResponse(reservtionPersist);
    }

    @Override
    public ReservationResponse read(UUID uuid) {
        ReservationEntity reservationFromDB = reservationRepository.findById(uuid)
                .orElseThrow();
        return entityToResponse(reservationFromDB);
    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID uuid) {
        HotelEntity hotel = hotelRepository.findById(request.getIdHotel())
                .orElseThrow();

        ReservationEntity reservationToUpdate = reservationRepository.findById(uuid)
                .orElseThrow();

        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_reservation)));

        ReservationEntity reservationUpdate = reservationRepository.save(reservationToUpdate);

        log.info("Reservation update " + reservationUpdate);

        return entityToResponse(reservationUpdate);
    }

    @Override
    public void delete(UUID uuid) {
        ReservationEntity reseravtionToDelete = reservationRepository.findById(uuid)
                .orElseThrow();
        reservationRepository.delete(reseravtionToDelete);
    }

    public static final BigDecimal charges_price_reservation = BigDecimal.valueOf(0.20);

    private ReservationResponse entityToResponse(ReservationEntity entity){
        ReservationResponse response = new ReservationResponse();
        BeanUtils.copyProperties(entity, response);
        HotelResponse hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);
        response.setHotel(hotelResponse);
        return response;
    }


    @Override
    public BigDecimal findPrice(Long hotelId) {
        HotelEntity hotel = hotelRepository.findById(hotelId).orElseThrow();
        return hotel.getPrice().add(hotel.getPrice().multiply(charges_price_reservation));
    }
}
