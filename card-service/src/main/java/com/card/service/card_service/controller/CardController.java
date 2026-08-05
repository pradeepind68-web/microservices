package com.card.service.card_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/card-api")
public class CardController {

    @PostMapping("/cards/validate")
    public Map<String, Object> checkCardValidity(@RequestBody Map<String,String> payload){
        String cardNumber = payload.get("cardNumber");
        String expiry= payload.get("expiry");
        boolean passesValidation = cardNumber != null && cardNumber.replaceAll("[\\s\\-]","").length() == 16;
        boolean isValid =false;
             List<String> expiryDate=Arrays.asList(expiry.split("/"));
             List<Integer> values = expiryDate.stream()
                     .map(Integer::parseInt)
                     .toList();
             isValid=
             values.size() >= 2 &&
             values.get(0) >= 1 &&
             values.get(0) <= 12 &&
             values.get(1) > 26;


        return Map.of(
                "valid", passesValidation && isValid,
                "cardChecked", cardNumber != null ? cardNumber : "NONE",
                "message", passesValidation && isValid ? "Validation Clear" : "Invalid Numeric Scheme or expiry"

        );

    }

}
