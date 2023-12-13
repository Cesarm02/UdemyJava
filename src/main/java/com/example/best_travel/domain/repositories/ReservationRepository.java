package com.example.best_travel.domain.repositories;

import com.example.best_travel.domain.entities.ReservationEntity;
import com.example.best_travel.domain.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ReservationRepository extends CrudRepository<ReservationEntity, UUID> {



}
