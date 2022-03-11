package com.example.zooplusServices.services;

import java.util.List;

public interface CurrencyService {
    List<String> getCurrencyExchangeRatePLNToCurrencyCode(String currencyCode1);
}
