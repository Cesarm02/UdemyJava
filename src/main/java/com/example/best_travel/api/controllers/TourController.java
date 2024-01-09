package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.request.TourRequest;
import com.example.best_travel.api.models.response.TourResponse;
import com.example.best_travel.infraestructure.abstrat.ITourService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
public class TourController {

    private final ITourService iTourService;

    @PostMapping
    public ResponseEntity<TourResponse> post(@RequestBody TourRequest tourRequest) {
        return ResponseEntity.ok(iTourService.create(tourRequest));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(iTourService.read(id));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        iTourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "{tourId}/remove_ticket/{ticketId}")
    public ResponseEntity<Void> removeTicket(@PathVariable Long tourId, @PathVariable UUID ticketId) {
        iTourService.removeTicket(tourId, ticketId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(@PathVariable Long tourId, @PathVariable Long flyId) {
        var response = Collections.singletonMap("ticketId", iTourService.addTicket(flyId, tourId));
        return ResponseEntity.ok(response);
    }

}
