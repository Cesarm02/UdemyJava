package com.example.best_travel.infraestructure.helper;

import com.example.best_travel.domain.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@Slf4j
@AllArgsConstructor
public class CustomerHelper {

    private final CustomerRepository customerRepository;

    public void incrase(String customerId,Class<?> type){
        var customertoUpdate = this.customerRepository.findById(customerId)
                .orElseThrow();

        switch (type.getSimpleName()){
            case "TourService" -> customertoUpdate.setTotalTours(customertoUpdate.getTotalTours() + 1);
            case "TicketService" -> customertoUpdate.setTotalFlights(customertoUpdate.getTotalFlights() + 1);
            case "ReservationService" -> customertoUpdate.setTotalLodgings(customertoUpdate.getTotalLodgings() + 1);
        }
        this.customerRepository.save(customertoUpdate);
    }

}
