package com.example.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "hotel")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HotelEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    public Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String address;
    private Integer rating;
    private BigDecimal price;

    //
    //Un vuelo tiene muchos tickets
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany
            (
                    cascade = CascadeType.ALL,
                    fetch = FetchType.EAGER,
                    orphanRemoval = true,
                    mappedBy = "hotel"
            )
    private Set<ReservationEntity> reservation;
}
