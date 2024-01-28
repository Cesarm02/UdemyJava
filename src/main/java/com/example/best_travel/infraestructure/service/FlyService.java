package com.example.best_travel.infraestructure.service;

import com.example.best_travel.api.models.CacheConstants;
import com.example.best_travel.api.models.response.FlyResponse;
import com.example.best_travel.domain.entities.jpa.FlyEntity;
import com.example.best_travel.domain.repositories.jpa.FlyRepository;
import com.example.best_travel.infraestructure.abstrat.IFlyService;
import com.example.best_travel.util.enums.SortType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j

public class FlyService implements IFlyService {

    private final FlyRepository flyRepository;
    private final WebClient webClient;

    public FlyService(FlyRepository flyRepository, @Qualifier(value = "base") WebClient webClient) {
        this.flyRepository = flyRepository;
        this.webClient = webClient;
    }

    @Override
    public Page<FlyResponse> realAll(Integer page, Integer size, SortType sort) {
        PageRequest pageRequest = null;
        switch (sort){
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        return this.flyRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    @Cacheable(value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readLessPrice(BigDecimal price) {
        return flyRepository.selectLessPrice(price)
                .stream().map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<FlyResponse> readBetweenPrices(BigDecimal min, BigDecimal max) {
        return flyRepository.selectBetweenPrice(min, max)
                .stream().map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<FlyResponse> readByOriginDestiny(String origin, String destiny)
    {

        return flyRepository.selectOriginAndDestiny(origin, destiny)
                .stream().map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    public FlyResponse entityToResponse(FlyEntity entity){
        FlyResponse flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity, flyResponse);
        return flyResponse;
    }

}
