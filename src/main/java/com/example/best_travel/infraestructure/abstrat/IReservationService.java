package com.example.best_travel.infraestructure.abstrat;

import com.example.best_travel.api.models.request.ReservationRequest;
import com.example.best_travel.api.models.response.ReservationResponse;

import java.util.UUID;

public interface IReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID>{


}
