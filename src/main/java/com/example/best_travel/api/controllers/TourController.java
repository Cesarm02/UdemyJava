package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.request.TourRequest;
import com.example.best_travel.api.models.response.ErrorResponse;
import com.example.best_travel.api.models.response.TourResponse;
import com.example.best_travel.infraestructure.abstrat.ITourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
@Tag(name = "Tour")

public class TourController {

    private final ITourService iTourService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @Operation(summary = "Save in system a tour based in list of hotels and fligths")
    @PostMapping
    public ResponseEntity<TourResponse> post(@RequestBody TourRequest tourRequest) {
        return ResponseEntity.ok(iTourService.create(tourRequest));
    }

    @Operation(summary = "Return a with ad passed")
    @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(iTourService.read(id));
    }

    @Operation(summary = "Delete a tour with id passed")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        iTourService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Remove a ticket from tour")
    @PatchMapping(path = "{tourId}/remove_ticket/{ticketId}")
    public ResponseEntity<Void> removeTicket(@PathVariable Long tourId, @PathVariable UUID ticketId) {
        iTourService.removeTicket(tourId, ticketId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add a ticket from tour")
    @PatchMapping(path = "{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(@PathVariable Long tourId, @PathVariable Long flyId) {
        var response = Collections.singletonMap("ticketId", iTourService.addTicket(flyId, tourId));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove a reservation from tour")
    @PatchMapping(path = "{tourId}/remove_reservation/{reseravationId}")
    public ResponseEntity<Void> removeReservation(@PathVariable Long tourId, @PathVariable UUID reseravationId) {
        iTourService.removeReservation(tourId, reseravationId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "add a reservation from tour")
    @PatchMapping(path = "{tourId}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String, UUID>> postReservation(
            @PathVariable Long tourId,
            @PathVariable Long hotelId,
            @RequestParam Integer totalDays) {
        var response = Collections.singletonMap("ticketId", iTourService.addReservation(hotelId, tourId, totalDays));
        return ResponseEntity.ok(response);
    }

}
