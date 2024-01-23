package com.example.best_travel.api.models.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketRequest implements Serializable {
    @Size(min = 18,max = 20, message = "The size have to a length between 18 and 20")
    @NotBlank(message = "Id client is mandatory")
    private String idClient;
    @Positive(message = "Must be greater than 0")
    @NotNull(message = "Id fly is mandatory")
    private Long idFly;

    @Email(message = "Invalid email")
    private String email;
}
