package pl.szlify.exchangeapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szlify.exchangeapi.model.TestModel;
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

    @GetMapping("/test")
    public TestModel test() {
        return currencyService.test();
    }

    @GetMapping("/convert")
    public ResponseEntity<BigDecimal> convertCurrency(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        BigDecimal result = currencyService.convertCurrency(from, to, amount, date);
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
}
