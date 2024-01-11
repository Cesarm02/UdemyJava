package com.example.best_travel.infraestructure.service;

import com.example.best_travel.api.models.response.HotelResponse;
import com.example.best_travel.domain.entities.HotelEntity;
import com.example.best_travel.domain.repositories.HotelRepository;
import com.example.best_travel.infraestructure.abstrat.IHotelService;
import com.example.best_travel.util.enums.SortType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;

    @Override
    public Page<HotelResponse> realAll(Integer page, Integer size, SortType sort) {
        PageRequest pageRequest = null;
        switch (sort){
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        return this.hotelRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    public Set<HotelResponse> readLessPrice(BigDecimal price) {
        return hotelRepository.findByPriceLessThan(price)
                .stream().map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> readBetweenPrices(BigDecimal min, BigDecimal max) {
        return hotelRepository.findByPriceBetween(min, max)
                .stream().map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> readByRating(Integer rating) {
        return hotelRepository.findByRatingGreaterThan(rating)
                .stream().map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    public HotelResponse entityToResponse(HotelEntity entity){
        HotelResponse hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity, hotelResponse);
        return hotelResponse;
    }
}
