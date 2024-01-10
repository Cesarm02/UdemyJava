package com.example.best_travel.infraestructure.abstrat;

import com.example.best_travel.api.models.request.TourRequest;
import com.example.best_travel.api.models.response.TourResponse;

import java.util.UUID;

public interface ITourService extends SimpleCrudService<TourRequest, TourResponse, Long>{

    void removeTicket(Long tourId, UUID ticketId);

    UUID addTicket(Long flyId, Long tourId);

    void removeReservation( Long tourId, UUID reservationId);

    UUID addReservation(Long flyId, Long reservationId, Integer totalDays);


}
