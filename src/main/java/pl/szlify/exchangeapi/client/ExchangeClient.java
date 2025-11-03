package pl.szlify.exchangeapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.model.dto.FluctuationDto;
import pl.szlify.exchangeapi.model.dto.HistoricalDateRatesDto;
import pl.szlify.exchangeapi.model.dto.LatestRatesDto;
import pl.szlify.exchangeapi.model.dto.SymbolsDto;
import pl.szlify.exchangeapi.model.dto.TimeSeriesRatesDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@FeignClient(name = "exchangeApiClient", url = "${exchange.api.base-url}")
public interface ExchangeClient {

//    @RequestLine("GET /convert?from={from}&to={to}&amount={amount}&date={data}")
//    CurrencyConversionDto currencyConversion(@Param("from") String from,
//                                             @Param("to") String to,
//                                             @Param("amount") BigDecimal amount,
//                                             @Param("data") LocalDate data);

    @GetMapping("/convert")
    CurrencyConversionDto currencyConversion(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

//    @RequestLine("GET /fluctuation?base={base}&symbols={symbols}&end_date={endDate}&start_date={startDate}")
//    FluctuationDto fluctuation(@Param("base") String base,
//                               @Param("symbols") String symbols,
//                               @Param("endDate") LocalDate endDate,
//                               @Param("startDate") LocalDate startDate);

    @GetMapping("/fluctuation")
    FluctuationDto fluctuation(
            @RequestParam("base") String base,
            @RequestParam(value = "symbols", required = false) String symbols,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate);

//    @RequestLine("GET /latest?base={base}&symbols={symbols}")
//    LatestRatesDto latestRates(@Param("base") String base,
//                               @Param("symbols") String symbols);

    @GetMapping("/latest")
    LatestRatesDto latestRates(
            @RequestParam("base") String base,
            @RequestParam(value = "symbols", required = false) String symbols);

//    @RequestLine("GET /symbols")
//    SymbolsDto symbols();

    @GetMapping("/symbols")
    SymbolsDto symbols();

//    @RequestLine("GET /timeseries?base={base}&symbols={symbols}&end_date={endDate}&start_date={startDate}")
//    TimeSeriesRatesDto timeseries(@Param("base") String base,
//                                   @Param("symbols") String symbols,
//                                   @Param("endDate") LocalDate endDate,
//                                   @Param("startDate") LocalDate startDate);

    @GetMapping("/timeseries")
    TimeSeriesRatesDto timeseries(
            @RequestParam("base") String base,
            @RequestParam(value = "symbols", required = false) String symbols,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate);

//    @RequestLine("GET /{date}?base={base}&symbols={symbols}")
//    HistoricalDateRatesDto historicalRates(@Param("date") LocalDate date,
//                                           @Param("base") String base,
//                                           @Param("symbols") String symbols);

    @GetMapping("/{date}")
    HistoricalDateRatesDto historicalRates(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("base") String base,
            @RequestParam(value = "symbols", required = false) String symbols);
}
