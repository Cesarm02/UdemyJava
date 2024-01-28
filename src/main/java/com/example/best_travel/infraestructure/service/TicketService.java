package com.example.best_travel.infraestructure.service;

import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.response.FlyResponse;
import com.example.best_travel.api.models.response.TicketReponse;
import com.example.best_travel.domain.entities.jpa.CustomerEntity;
import com.example.best_travel.domain.entities.jpa.FlyEntity;
import com.example.best_travel.domain.entities.jpa.TicketEntity;
import com.example.best_travel.domain.repositories.jpa.CustomerRepository;
import com.example.best_travel.domain.repositories.jpa.FlyRepository;
import com.example.best_travel.domain.repositories.jpa.ReservationRepository;
import com.example.best_travel.domain.repositories.jpa.TicketRepository;
import com.example.best_travel.infraestructure.abstrat.ITicketService;
import com.example.best_travel.infraestructure.helper.BlackListHelper;
import com.example.best_travel.infraestructure.helper.CustomerHelper;
import com.example.best_travel.infraestructure.helper.EmailHelper;
import com.example.best_travel.util.BestTravelUtil;
import com.example.best_travel.util.enums.Tables;
import com.example.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
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

    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;

    private final EmailHelper emailHelper;

    @Override
    public TicketReponse create(TicketRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());

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
                .price(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomSoon())
                .build();

        TicketEntity ticketGuardado = this.ticketRepository.save(ticket);
        this.customerHelper.incrase(customer.getDni(), TicketService.class);
        log.info("Ticket guardado "  + ticket);

        if(Objects.nonNull(request.getEmail()))
            this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.ticket.name());

        return this.entityToResponse(ticket);
    }

    @Override
    public TicketReponse read(UUID uuid) {
        TicketEntity ticketFromDB = this.ticketRepository.findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));
        return this.entityToResponse(ticketFromDB);
    }

    @Override
    public TicketReponse update(TicketRequest request, UUID uuid) {

        TicketEntity ticketTOUpdate = ticketRepository.findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));

        FlyEntity fly = flyRepository.findById(request.getIdFly())
                .orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));

        ticketTOUpdate.setFly(fly);
        ticketTOUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)));
        ticketTOUpdate.setArrivalDate(BestTravelUtil.getRandomLater());
        ticketTOUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());

        TicketEntity ticketUpdate = this.ticketRepository.save(ticketTOUpdate);
        log.info("Actualizado " + ticketUpdate);
        return this.entityToResponse(ticketUpdate);
    }

    @Override
    public void delete(UUID uuid) {
        TicketEntity ticketToDelete = ticketRepository.findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));
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

    @Override
    public BigDecimal findPrice(Long idFly) {
        FlyEntity fly = this.flyRepository.findById(idFly).orElseThrow();
        return fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage));
    }

    public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(0.25);
}
