package pl.szlify.exchangeapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szlify.exchangeapi.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.dto.CurrencySymbolsDto;
import pl.szlify.exchangeapi.dto.FluctuationDto;
import pl.szlify.exchangeapi.dto.LatestRatesDto;
import pl.szlify.exchangeapi.model.TestModel;
import pl.szlify.exchangeapi.service.CurrencyService;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    public final CurrencyService currencyService;


    @GetMapping("/test")
    public TestModel test() {
        return currencyService.getTestModel();
    }

    @GetMapping("/convert")
    public ResponseEntity<CurrencyConversionDto> convertCurrency(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        CurrencyConversionDto result = currencyService.convertCurrency(from, to, amount, date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/symbols")
    public ResponseEntity<CurrencySymbolsDto> getAllCurrencySymbols() {
        return ResponseEntity.ok(currencyService.getAllCurrencySymbols());
    }

    @GetMapping("/fluctuation")
    public ResponseEntity<FluctuationDto> getFluctuation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String base,
            @RequestParam(required = false) String symbols) {
        return ResponseEntity.ok(currencyService.getFluctuation(startDate, endDate, base, symbols));
    }

    @GetMapping("/latest")
    public ResponseEntity<LatestRatesDto> getLatestRates(
            @RequestParam(required = false) String base,
            @RequestParam(required = false) String symbols) {
        return ResponseEntity.ok(currencyService.getLatestRates(base, symbols));
    }
}
