package com.example.best_travel.infraestructure.abstrat;

import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.response.TicketReponse;

import java.util.UUID;

public interface ITicketService extends CrudService<TicketRequest, TicketReponse, UUID>{




}
