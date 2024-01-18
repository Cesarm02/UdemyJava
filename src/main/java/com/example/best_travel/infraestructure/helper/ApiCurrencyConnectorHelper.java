package com.example.best_travel.infraestructure.helper;

import com.example.best_travel.infraestructure.dto.CurrencyDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Currency;

@Component
public class ApiCurrencyConnectorHelper {

    private final WebClient currencyWebClient;
    @Value(value = "${api.base_currency}")
    private String baseCurrency;


    public ApiCurrencyConnectorHelper(WebClient currencyWebClient) {
        this.currencyWebClient = currencyWebClient;
    }

    private static final String BASE_CURRENCY = "?base={base}";
    private static final String SYMBOL_CURRENCY = "&symbols={symbols}";
    private static final String CURRENCY_PATH = "/exchangerates_data/latest";

    public CurrencyDto getCurrencyDto(Currency currency){
        return this.currencyWebClient
                .get()
                .uri(uri-> uri.path(CURRENCY_PATH)
                        .query(BASE_CURRENCY)
                        .query(SYMBOL_CURRENCY)
                        .build(baseCurrency, currency.getCurrencyCode())
                )
                .retrieve()
                .bodyToMono(CurrencyDto.class)
                .block();

    }
}
