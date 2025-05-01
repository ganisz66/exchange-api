package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.szlify.exchangeapi.client.ExchangeClient;
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

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final ExchangeClient exchangeClient;
    private final EmailService emailService;

    public CurrencyConversionDto convertCurrency(ConvertCommand command) {
        CurrencyConversionDto convertResult = exchangeClient.currencyConversion(command.getFrom(),
                command.getTo(),
                command.getAmount(),
                command.getDate());
        emailService.send("jankowalksiii12@gmail.com", convertResult);
        return convertResult;
    }

    public FluctuationDto getFluctuation(FluctuationCommand command) {
        String symbolsParam = command.getSymbols() == null || command.getSymbols().isEmpty()
                ? null
                : String.join(",", command.getSymbols());

        return exchangeClient.fluctuation(
                command.getBase(),
                symbolsParam,
                command.getEndDate(),
                command.getStartDate());
    }

    public LatestRatesDto getLatestRates(LatestCommand command) {
        String symbolsParam = command.getSymbols() == null || command.getSymbols().isEmpty()
                ? null
                : String.join(",", command.getSymbols());

        return exchangeClient.latestRates(command.getBaseCurrency(), symbolsParam);
    }

    @Cacheable("symbols")
    public SymbolsDto getSymbols() {
        System.out.println(">>> Wywołanie metody getSymbols() — brak cache");
        return exchangeClient.symbols();
    }

    public TimeSeriesRatesDto getTimeSeries(TimeseriesCommand command) {
        String symbolsParam = command.getSymbols() == null || command.getSymbols().isEmpty()
                ? null
                : String.join(",", command.getSymbols());

        return exchangeClient.timeseries(
                command.getBase(),
                symbolsParam,
                command.getEndDate(),
                command.getStartDate());
    }

    public HistoricalDateRatesDto getHistoricalRates(HistoricalDateCommand command) {
        String symbolsParam = command.getSymbols() == null || command.getSymbols().isEmpty()
                ? null
                : String.join(",", command.getSymbols());

        return exchangeClient.historicalRates(
                command.getDate(),
                command.getBaseCurrency(),
                symbolsParam);
    }
}
