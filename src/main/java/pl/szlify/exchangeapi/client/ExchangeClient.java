package pl.szlify.exchangeapi.client;

import feign.Param;
import feign.RequestLine;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.model.dto.FluctuationDto;
import pl.szlify.exchangeapi.model.dto.HistoricalDateRatesDto;
import pl.szlify.exchangeapi.model.dto.LatestRatesDto;
import pl.szlify.exchangeapi.model.dto.SymbolsDto;
import pl.szlify.exchangeapi.model.dto.TimeSeriesRatesDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExchangeClient {

    @RequestLine("GET /convert?from={from}&to={to}&amount={amount}&date={data}")
    CurrencyConversionDto currencyConversion(@Param("from") String from,
                                             @Param("to") String to,
                                             @Param("amount") BigDecimal amount,
                                             @Param("data") LocalDate data);

    @RequestLine("GET /fluctuation?base={base}&symbols={symbols}&end_date={endDate}&start_date={startDate}")
    FluctuationDto fluctuation(@Param("base") String base,
                               @Param("symbols") String symbols,
                               @Param("endDate") LocalDate endDate,
                               @Param("startDate") LocalDate startDate);

    @RequestLine("GET /latest?base={base}&symbols={symbols}")
    LatestRatesDto latestRates(@Param("base") String base,
                               @Param("symbols") String symbols);

    @RequestLine("GET /symbols")
    SymbolsDto symbols();

    @RequestLine("GET /timeseries?base={base}&symbols={symbols}&end_date={endDate}&start_date={startDate}")
    TimeSeriesRatesDto timeseries(@Param("base") String base,
                                   @Param("symbols") String symbols,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("startDate") LocalDate startDate);

    @RequestLine("GET /{date}?base={base}&symbols={symbols}")
    HistoricalDateRatesDto historicalRates(@Param("date") LocalDate date,
                                           @Param("base") String base,
                                           @Param("symbols") String symbols);
}
