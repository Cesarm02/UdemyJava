package com.example.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tour")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany
            (
                    cascade = CascadeType.ALL,
                    fetch = FetchType.EAGER,
                    orphanRemoval = true,
                    mappedBy = "tour"
            )
    private Set<ReservationEntity> reservations;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany
            (
                    cascade = CascadeType.ALL,
                    fetch = FetchType.EAGER,
                    orphanRemoval = true,
                    mappedBy = "tour"
            )
    private Set<TicketEntity> tickets;
    @ManyToOne
    @JoinColumn(name = "id_customer")
    private CustomerEntity customer;

    @PrePersist
    @PreRemove
    public void updateFk(){
        this.reservations.forEach(reservation -> reservation.setTour(this));
        this.tickets.forEach(ticket -> ticket.setTour(this));

    }

    public void removeTicket(UUID id){
        tickets.forEach(
                ticket-> {
                    if(ticket.getId().equals(id)){
                        ticket.setTour(null);
                    }
                }
        );
    }

    public void addTicket(TicketEntity ticket){
        if(Objects.isNull(this.tickets))
            this.tickets = new HashSet<>();
        this.tickets.add(ticket);
        this.tickets.forEach(ticket1 -> ticket1.setTour(this));
    }

    /*
    //Antes de reover
    //@PreRemove
    //Antes de actualizar
    //@PreUpdate
    //Antes de guardar
    //@PrePersist
    public void addTicket(TicketEntity ticket){
        if(Objects.isNull(this.tickets))
            this.tickets = new HashSet<>();
        this.tickets.add(ticket);
    }

    public void removeTicket(UUID uuid ){
        if(Objects.isNull(this.tickets))
            this.tickets = new HashSet<>();
        this.tickets.removeIf(ticket -> ticket.getId().equals(uuid));
    }

    public void updateTickets(){
        this.tickets.forEach(ticket -> ticket.setTour(this));
    }

    public void addReservations(ReservationEntity reservation){
        if(Objects.isNull(this.reservations))
            this.reservations = new HashSet<>();

        this.reservations.add(reservation);
    }

    public void removeReservation(UUID uuid ){
        if(Objects.isNull(this.reservations))
            this.reservations = new HashSet<>();

        this.reservations.removeIf(ticket -> ticket.getId().equals(uuid));
    }

    public void updateReservation(){
        this.reservations.forEach(ticket -> ticket.setTour(this));
    }*/
}
