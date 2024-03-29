package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.response.ErrorResponse;
import com.example.best_travel.api.models.response.TicketReponse;
import com.example.best_travel.infraestructure.abstrat.ITicketService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
@Tag(name = "Ticket")

public class TicketController {

    private final ITicketService ticketService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @PostMapping
    public ResponseEntity<TicketReponse> postTicket(@Valid  @RequestBody TicketRequest ticketRequest){
        return ResponseEntity.ok(ticketService.create(ticketRequest));
    }

    @GetMapping(path = "{uuid}")
    public ResponseEntity<TicketReponse> getTicket(@PathVariable UUID uuid){
        return ResponseEntity.ok(ticketService.read(uuid));
    }

    @PutMapping(path = "{uuid}")
    public ResponseEntity<TicketReponse> putTicket(@Valid @PathVariable UUID uuid, @RequestBody TicketRequest ticketRequest){
        return ResponseEntity.ok(ticketService.update(ticketRequest, uuid));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id){
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getFlyPrice(@RequestParam Long flyId){
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", ticketService.findPrice(flyId)));
    }


}
