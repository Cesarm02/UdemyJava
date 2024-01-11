package com.example.best_travel.util.exceptions;

public class FordibbenCustomerException extends  RuntimeException{

    public FordibbenCustomerException(){
        super("This customer is blocked");
    }

}
