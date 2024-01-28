package com.example.best_travel.domain.entities.jpa;

import com.example.best_travel.util.enums.AeroLine;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "fly")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double originLat;
    private Double originLng;
    private Double destinyLat;
    private Double destinyLng;
    private BigDecimal price;
    @Column(length = 20)
    private String originName;
    @Column(length = 20)
    private String destinyName;
    @Enumerated(EnumType.STRING)
    private AeroLine aeroLine;

    //Un vuelo tiene muchos tickets
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany
            (
                    cascade = CascadeType.ALL,
                    fetch = FetchType.EAGER,
                    orphanRemoval = true,
                    mappedBy = "fly"
            )
    private Set<TicketEntity> tickets;

}
