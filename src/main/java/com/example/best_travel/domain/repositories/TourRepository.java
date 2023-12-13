package com.example.best_travel.domain.repositories;

import com.example.best_travel.domain.entities.TourEntity;
import org.springframework.data.repository.CrudRepository;

public interface TourRepository extends CrudRepository<TourEntity, Long> {



}
