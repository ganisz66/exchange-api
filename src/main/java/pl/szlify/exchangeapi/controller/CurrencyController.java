package pl.szlify.exchangeapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szlify.exchangeapi.model.command.ConvertCommand;
import pl.szlify.exchangeapi.model.command.FluctuationCommand;
import pl.szlify.exchangeapi.model.command.HistoricalRatesCommand;
import pl.szlify.exchangeapi.model.command.LatestRatesCommand;
import pl.szlify.exchangeapi.model.command.TimeSeriesRatesCommand;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.model.dto.CurrencySymbolsDto;
import pl.szlify.exchangeapi.model.dto.FluctuationDto;
import pl.szlify.exchangeapi.model.dto.HistoricalRatesDto;
import pl.szlify.exchangeapi.model.dto.LatestRatesDto;
import pl.szlify.exchangeapi.model.dto.TimeSeriesDto;
import pl.szlify.exchangeapi.service.CurrencyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    public final CurrencyService currencyService;

    // Api should not return simple data as: String, BigDecimal etc. Simple data is not serializable into JSON. Deserialization will be required.

    @GetMapping("/convert")
    public ResponseEntity<CurrencyConversionDto> convertCurrency(@Valid ConvertCommand command) {
        return ResponseEntity.ok(currencyService.convertCurrency(command));
    }

    @GetMapping("/symbols")
    public ResponseEntity<CurrencySymbolsDto> getAllCurrencySymbols() {
        return ResponseEntity.ok(currencyService.getAllCurrencySymbols());
    }

    @GetMapping("/fluctuation")
    public ResponseEntity<FluctuationDto> getFluctuation(@Valid FluctuationCommand command) {
        return ResponseEntity.ok(currencyService.getFluctuation(command));
    }

    @GetMapping("/latest")
    public ResponseEntity<LatestRatesDto> getLatestRates(@Valid LatestRatesCommand command) {
        return ResponseEntity.ok(currencyService.getLatestRates(command));
    }

    @GetMapping("/timeseries")
    public ResponseEntity<TimeSeriesDto> getTimeSeries(@Valid TimeSeriesRatesCommand command) {
        return ResponseEntity.ok(currencyService.getTimeSeries(command));
    }

    @GetMapping("/{date}")
    public ResponseEntity<HistoricalRatesDto> getHistoricalRates(@Valid HistoricalRatesCommand command) {
        return ResponseEntity.ok(currencyService.getHistoricalRates(command));
    }
}
