package com.example.best_travel.domain.repositories.jpa;

import com.example.best_travel.domain.entities.jpa.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    Set<HotelEntity> findByPriceLessThan(BigDecimal price);
    Set<HotelEntity> findByPriceBetween(BigDecimal min, BigDecimal max);
    Set<HotelEntity> findByRatingGreaterThan(int rating);
    Optional<HotelEntity> findByReservationId(UUID id);
}
