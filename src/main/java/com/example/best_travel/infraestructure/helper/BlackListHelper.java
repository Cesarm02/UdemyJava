package com.example.best_travel.infraestructure.helper;

import com.example.best_travel.util.exceptions.FordibbenCustomerException;
import org.springframework.stereotype.Component;

@Component
public class BlackListHelper {

    public void isInBlackListCustomer(String id){
        if(id.equals("WALA771012HCRGR054"))
            throw new FordibbenCustomerException();
    }

}
