package com.example.best_travel.api.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourRequest implements Serializable {
    @Size(min = 18,max = 20, message = "The size have to a length between 18 and 20")
    @NotBlank(message = "Id client is mandatory")
    public String customerId;
    private Set<TourFlyRequest> flights;
    private Set<TourHotelRequest> hotels;
    @Email(message = "Invalid email")
    private String email;

}
