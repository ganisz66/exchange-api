package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.szlify.exchangeapi.client.ExchangeClient;
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

import java.lang.reflect.Method;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final ExchangeClient exchangeClient;
    private final EmailService emailService;

    @Cacheable("symbols")
    public CurrencySymbolsDto getAllCurrencySymbols() {
        return exchangeClient.getCurrencySymbols();
    }

    public CurrencyConversionDto convertCurrency(ConvertCommand command) {
        CurrencyConversionDto convertResult = exchangeClient.getCurrencyConversion(command.getFrom(),
                command.getTo(),
                command.getAmount(),
                command.getDate());
        emailService.send("jankowalksiii12@gmail.com", convertResult);
        return convertResult;
    }

    public FluctuationDto getFluctuation(FluctuationCommand command) {
        return exchangeClient.getFluctuation(command.getStart_date(),
                command.getEnd_date(),
                command.getBase(),
                validateSymbols(command));
    }

    public LatestRatesDto getLatestRates(LatestRatesCommand command) {
        return exchangeClient.getLatest(command.getBase(), validateSymbols(command));
    }

    public TimeSeriesDto getTimeSeries(TimeSeriesRatesCommand command) {
        return exchangeClient.getTimeSeries(command.getStartDate(),
                command.getEndDate(),
                command.getBase(),
                validateSymbols(command));
    }

    public HistoricalRatesDto getHistoricalRates(HistoricalRatesCommand command) {
        return exchangeClient.getHistoricalRates(command.getDate(),
                command.getBase(),
                validateSymbols(command));
    }

    private <T> String validateSymbols(T command) {
        try {
            Method getSymbolsMethod = command.getClass().getMethod("getSymbols");
            @SuppressWarnings("unchecked")
            Collection<String> symbols = (Collection<String>) getSymbolsMethod.invoke(command);
            return symbols == null || symbols.isEmpty() ? null : String.join(",", symbols);
        } catch (Exception e) {
            return null;
        }
    }
}



