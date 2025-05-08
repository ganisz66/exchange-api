package pl.szlify.exchangeapi.client;

import feign.Param;
import feign.RequestLine;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.model.dto.CurrencySymbolsDto;
import pl.szlify.exchangeapi.model.dto.FluctuationDto;
import pl.szlify.exchangeapi.model.dto.HistoricalRatesDto;
import pl.szlify.exchangeapi.model.dto.LatestRatesDto;
import pl.szlify.exchangeapi.model.dto.TimeSeriesDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExchangeClient {

    @RequestLine("GET /convert?from={from}&to={to}&amount={amount}&date={data}")
    CurrencyConversionDto getCurrencyConversion(@Param("from") String from,
                                                @Param("to") String to,
                                                @Param("amount") BigDecimal amount,
                                                @Param("data") LocalDate data);

    @RequestLine("GET /symbols")
    CurrencySymbolsDto getCurrencySymbols();

    @RequestLine("GET /fluctuation?startDate={startDate}&endDate={endDate}&base={base}&symbols={symbols}")
    FluctuationDto getFluctuation(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("base") String base,
            @Param("symbols") String symbols
    );

    @RequestLine("GET /latest?base={base}&symbols={symbols}")
    LatestRatesDto getLatest(@Param("base") String base,
                             @Param("symbols") String symbols);

    @RequestLine("GET /timeseries?startDate={startDate}&endDate={endDate}&base={base}&symbols={symbols}")
    TimeSeriesDto getTimeSeries(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("base") String base,
                                @Param("symbols") String symbols);

    @RequestLine("GET /{date}?base={base}&symbols={symbols}")
    HistoricalRatesDto getHistoricalRates(@Param("date") LocalDate date,
                                          @Param("base") String base,
                                          @Param("symbols") String symbols);

}
