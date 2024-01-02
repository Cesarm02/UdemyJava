package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.response.TicketReponse;
import com.example.best_travel.infraestructure.abstrat.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
public class TicketController {

    private final ITicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketReponse> postTicket(@RequestBody TicketRequest ticketRequest){
        return ResponseEntity.ok(ticketService.create(ticketRequest));
    }


}
