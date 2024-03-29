package com.example.best_travel.api.controllers;

import com.example.best_travel.api.models.response.HotelResponse;
import com.example.best_travel.infraestructure.abstrat.IHotelService;
import com.example.best_travel.util.annotations.Notify;
import com.example.best_travel.util.enums.SortType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "hotel")
@AllArgsConstructor
@Tag(name = "Hotel")

public class HotelController {

    private final IHotelService hotelService;

    @GetMapping
    @Notify(value = "Hotel request")
    public ResponseEntity<Page<HotelResponse>> getAllFly(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType){
        if(Objects.isNull(sortType))
            sortType = SortType.NONE;
        var response = hotelService.realAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "less_price")
    public ResponseEntity<Set<HotelResponse>> getLessPrice(
            @RequestParam BigDecimal price){
        var response = hotelService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "between_price")
    public ResponseEntity<Set<HotelResponse>> getBetweenPrice(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max){
        var response = hotelService.readBetweenPrices(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "rating")
    public ResponseEntity<Set<HotelResponse>> getRating(
            @RequestParam Integer rating){
        if(rating > 4) rating=4;
        if(rating < 1) rating=1;
        var response = hotelService.readByRating(rating);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }


}
