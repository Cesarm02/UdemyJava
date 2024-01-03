package com.example.best_travel.infraestructure.service;

import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.response.FlyResponse;
import com.example.best_travel.api.models.response.TicketReponse;
import com.example.best_travel.domain.entities.CustomerEntity;
import com.example.best_travel.domain.entities.FlyEntity;
import com.example.best_travel.domain.entities.TicketEntity;
import com.example.best_travel.domain.repositories.CustomerRepository;
import com.example.best_travel.domain.repositories.FlyRepository;
import com.example.best_travel.domain.repositories.ReservationRepository;
import com.example.best_travel.domain.repositories.TicketRepository;
import com.example.best_travel.infraestructure.abstrat.ITicketService;
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
public class TicketService implements ITicketService {
    private final ReservationRepository reservationRepository;

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;


    @Override
    public TicketReponse create(TicketRequest request) {
        FlyEntity fly = flyRepository.findById(request.getIdFly())
                .orElseThrow(
                        IllegalStateException::new
                );
        CustomerEntity customer = customerRepository.findById(request.getIdClient())
                .orElseThrow(
                        IllegalStateException::new
                );

        TicketEntity ticket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(BigDecimal.valueOf(0.25)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDate.now())
                .departureDate(LocalDate.now())
                .build();

        TicketEntity ticketGuardado = this.ticketRepository.save(ticket);
        log.info("Ticket guardado "  + ticket);

        return this.entityToResponse(ticket);
    }

    @Override
    public TicketReponse read(UUID uuid) {
        TicketEntity ticketFromDB = this.ticketRepository.findById(uuid)
                .orElseThrow();
        return this.entityToResponse(ticketFromDB);
    }

    @Override
    public TicketReponse update(TicketRequest request, UUID uuid) {

        TicketEntity ticketTOUpdate = ticketRepository.findById(uuid)
                .orElseThrow();

        FlyEntity fly = flyRepository.findById(request.getIdFly())
                .orElseThrow();

        ticketTOUpdate.setFly(fly);
        ticketTOUpdate.setPrice(BigDecimal.valueOf(0.25));
        ticketTOUpdate.setArrivalDate(LocalDate.now());
        ticketTOUpdate.setDepartureDate(LocalDate.now());

        TicketEntity ticketUpdate = this.ticketRepository.save(ticketTOUpdate);
        log.info("Actualizado " + ticketUpdate);
        return this.entityToResponse(ticketUpdate);
    }

    @Override
    public void delete(UUID uuid) {
        TicketEntity ticketToDelete = ticketRepository.findById(uuid)
                .orElseThrow();
        this.ticketRepository.delete(ticketToDelete);
    }

    private TicketReponse entityToResponse(TicketEntity entity){
        TicketReponse ticketReponse = new TicketReponse();
        BeanUtils.copyProperties(entity, ticketReponse);
        FlyResponse flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity, flyResponse);
        flyResponse.setId(entity.getFly().getId());
        flyResponse.setOriginLat(entity.getFly().getOriginLat());
        flyResponse.setOriginLng(entity.getFly().getOriginLng());
        flyResponse.setDestinyLat(entity.getFly().getDestinyLat());
        flyResponse.setDestinyLng(entity.getFly().getDestinyLng());
        flyResponse.setOriginName(entity.getFly().getOriginName());
        flyResponse.setDestinyName(entity.getFly().getDestinyName());
        flyResponse.setAeroLine(entity.getFly().getAeroLine());
        flyResponse.setPrice(entity.getFly().getPrice());
        ticketReponse.setFly(flyResponse);
        return ticketReponse;
    }

}
