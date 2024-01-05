package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.request.ReservationRequest;
import com.example.best_travel.api.models.request.TicketRequest;
import com.example.best_travel.api.models.response.ReservationResponse;
import com.example.best_travel.api.models.response.TicketReponse;
import com.example.best_travel.infraestructure.abstrat.IReservationService;
import com.example.best_travel.infraestructure.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
public class ReservationController {

    @Autowired
    private final IReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> postReservation(@RequestBody ReservationRequest request){
        return ResponseEntity.ok(reservationService.create(request));
    }

    @GetMapping(path = "{uuid}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable UUID uuid){
        return ResponseEntity.ok(reservationService.read(uuid));
    }

    @PutMapping(path = "{uuid}")
    public ResponseEntity<ReservationResponse> putReservation(@PathVariable UUID uuid, @RequestBody ReservationRequest request){
        return ResponseEntity.ok(reservationService.update(request, uuid));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id){
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getFlyPrice(@RequestParam Long flyId){
        return ResponseEntity.ok(Collections.singletonMap("hotelPrice", reservationService.findPrice(flyId)));
    }
}
