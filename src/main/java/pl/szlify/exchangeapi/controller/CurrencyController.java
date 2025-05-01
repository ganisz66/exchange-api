package pl.szlify.exchangeapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szlify.exchangeapi.model.command.ConvertCommand;
import pl.szlify.exchangeapi.model.command.FluctuationCommand;
import pl.szlify.exchangeapi.model.command.HistoricalDateCommand;
import pl.szlify.exchangeapi.model.command.LatestCommand;
import pl.szlify.exchangeapi.model.command.TimeseriesCommand;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.model.dto.FluctuationDto;
import pl.szlify.exchangeapi.model.dto.HistoricalDateRatesDto;
import pl.szlify.exchangeapi.model.dto.LatestRatesDto;
import pl.szlify.exchangeapi.model.dto.SymbolsDto;
import pl.szlify.exchangeapi.model.dto.TimeSeriesRatesDto;
import pl.szlify.exchangeapi.service.CurrencyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    public final CurrencyService currencyService;

    @GetMapping("/convert")
    public ResponseEntity<CurrencyConversionDto> convertCurrency(@Valid ConvertCommand command) {
        return ResponseEntity.ok(currencyService.convertCurrency(command));
    }

    @GetMapping("/fluctuation")
    public ResponseEntity<FluctuationDto> fluctuation(@Valid FluctuationCommand command) {
        return ResponseEntity.ok(currencyService.getFluctuation(command));
    }

    @GetMapping("/latest")
    public ResponseEntity<LatestRatesDto> getLatestRates(@Valid LatestCommand command) {
        return ResponseEntity.ok(currencyService.getLatestRates(command));
    }

    @GetMapping("/symbols")
    public ResponseEntity<SymbolsDto> getSymbols() {
        return ResponseEntity.ok(currencyService.getSymbols());
    }

    @GetMapping("/timeseries")
    public ResponseEntity<TimeSeriesRatesDto> getTimeSeries(@Valid TimeseriesCommand command) {
        return ResponseEntity.ok(currencyService.getTimeSeries(command));
    }

    @GetMapping("/{date}")
    public ResponseEntity<HistoricalDateRatesDto> getHistoricalRates(@Valid HistoricalDateCommand command) {
        return ResponseEntity.ok(currencyService.getHistoricalRates(command));
    }
}
