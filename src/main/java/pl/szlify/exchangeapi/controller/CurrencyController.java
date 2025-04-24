package pl.szlify.exchangeapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szlify.exchangeapi.model.TestModel;
import pl.szlify.exchangeapi.model.command.ConvertCommand;
import pl.szlify.exchangeapi.model.dto.CurrencyRateDto;
import pl.szlify.exchangeapi.service.CurrencyService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    public final CurrencyService currencyService;

    @GetMapping("/convert")
    public ResponseEntity<BigDecimal> convertCurrency(@Valid ConvertCommand command) {
        BigDecimal result = currencyService.convertCurrency(command);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/fluctuation")
    public ResponseEntity<Map<String, CurrencyRateDto>> fluctuation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) String base,
            @RequestParam(required = false) List<String> symbols
    ) {
        return ResponseEntity.ok(currencyService.getFluctuation(startDate, endDate, base, symbols));
    }

    @GetMapping("/latest")
    public ResponseEntity<Map<String, Double>> getLatestRates(
            @RequestParam(required = false) String baseCurrency,
            @RequestParam(required = false) List<String> symbols
    ) {
        return ResponseEntity.ok(currencyService.getLatestRates(baseCurrency, symbols));
    }

    @GetMapping("/symbols")
    public ResponseEntity<Map<String, String>> getSymbols() {
        return ResponseEntity.ok(currencyService.getSymbols());
    }

    @GetMapping("/timeseries")
    public ResponseEntity<Map<String, Map<String, Double>>> getTimeSeries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) String base,
            @RequestParam(required = false) List<String> symbols
    ) {
        return ResponseEntity.ok(currencyService.getTimeSeries(endDate, startDate, base, symbols));
    }

    @GetMapping("/{date}")
    public ResponseEntity<Map<String, Double>> getHistoricalRates(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String base,
            @RequestParam(required = false) List<String> symbols
    ) {
        return ResponseEntity.ok(currencyService.getHistoricalRates(date, base, symbols));
    }
}
