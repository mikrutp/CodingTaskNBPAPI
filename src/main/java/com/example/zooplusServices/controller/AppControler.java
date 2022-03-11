package com.example.zooplusServices.controller;

import com.example.zooplusServices.services.CurrencyService;
import com.example.zooplusServices.services.GoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppControler {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private GoldService goldService;

    @GetMapping("/api/gold-price/avarage")
    public ResponseEntity<String> getGoldService() {
        String answear = goldService.getAvarageGoldFor14Days();
        return new ResponseEntity<>(answear, HttpStatus.OK);
    }

    @GetMapping("/api/exchange-rates/{currencyCode}")

    public ResponseEntity<List<String>> getMoneyServicePerCode(Model model, @PathVariable("currencyCode") String currencyCode) {
        List<String> answearList = currencyService.getCurrencyExchangeRatePLNToCurrencyCode(currencyCode);
        return new ResponseEntity<>(answearList, HttpStatus.OK);
    }
}
