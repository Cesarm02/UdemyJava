package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.request.ReservationRequest;
import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.response.ErrorResponse;
import com.example.best_travel.api.models.response.ReservationResponse;
import com.example.best_travel.api.models.response.TicketReponse;
import com.example.best_travel.infraestructure.abstrat.IReservationService;
import com.example.best_travel.infraestructure.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
@Tag(name = "Reservation")
public class ReservationController {

    @Autowired
    private final IReservationService reservationService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )

    @PostMapping
    public ResponseEntity<ReservationResponse> postReservation(@Valid @RequestBody ReservationRequest request){
        return ResponseEntity.ok(reservationService.create(request));
    }

    @GetMapping(path = "{uuid}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable UUID uuid){
        return ResponseEntity.ok(reservationService.read(uuid));
    }

    @PutMapping(path = "{uuid}")
    public ResponseEntity<ReservationResponse> putReservation(@Valid @PathVariable UUID uuid, @RequestBody ReservationRequest request){
        return ResponseEntity.ok(reservationService.update(request, uuid));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id){
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Return a reservation price")
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getReservationPrice(@RequestParam Long flyId, @RequestHeader(required = false) Currency currency){
        if(Objects.isNull(currency))
            currency = Currency.getInstance("USD");
        return ResponseEntity.ok(Collections.singletonMap("hotelPrice", reservationService.findPrice(flyId, currency)));
    }


}
