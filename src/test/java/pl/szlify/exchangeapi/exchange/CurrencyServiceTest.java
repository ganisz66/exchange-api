package pl.szlify.exchangeapi.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import pl.szlify.exchangeapi.service.CurrencyService;
import pl.szlify.exchangeapi.service.EmailService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {


    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private ExchangeClient exchangeClient;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

    @Captor
    private ArgumentCaptor<CurrencyConversionDto> conversionDtoCaptor;

    @Mock
    private EmailService emailService;

    @Test
    void testGetAllCurrencySymbols_ResultsInSymbolsBeingRetrieved() {

        // given
        CurrencySymbolsDto expectedSymbols = CurrencySymbolsDto.builder().build();
        expectedSymbols.setSymbols(Map.of(
                "USD", "US Dollar",
                "EUR", "Euro",
                "GBP", "British Pound"
        ));

        when(exchangeClient.getCurrencySymbols()).thenReturn(expectedSymbols);

        // when
        CurrencySymbolsDto result = currencyService.getAllCurrencySymbols();
        CurrencySymbolsDto cachedResult = currencyService.getAllCurrencySymbols();

        // tthen
        assertEquals(expectedSymbols, result);
        verify(exchangeClient, times(2)).getCurrencySymbols();
        assertEquals(expectedSymbols, cachedResult);
        verify(exchangeClient, times(2)).getCurrencySymbols();
    }

    @Test
    void testConvertCurrency_ReturnsCurrencyConversionFromClientAndSendsEmail() {
        // given
        ConvertCommand command = ConvertCommand.builder()
                .from("USD")
                .to("EUR")
                .amount(new BigDecimal("100.00"))
                .date(LocalDate.parse("2025-05-08"))
                .build();

        CurrencyConversionDto.ConversionQuery query = CurrencyConversionDto.ConversionQuery.builder()
                .amount(new BigDecimal("100.00"))
                .from("USD")
                .to("EUR")
                .build();

        CurrencyConversionDto.ConversionInfo info = CurrencyConversionDto.ConversionInfo.builder()
                .rate(new BigDecimal("0.85"))
                .timestamp(1683532800000L)
                .build();

        CurrencyConversionDto expectedConversion = CurrencyConversionDto.builder()
                .date("2025-05-08")
                .info(info)
                .query(query)
                .result(new BigDecimal("85.00"))
                .success(true)
                .build();

        when(exchangeClient.getCurrencyConversion(
                command.getFrom(),
                command.getTo(),
                command.getAmount(),
                command.getDate()
        )).thenReturn(expectedConversion);

        doNothing().when(emailService).send(anyString(), any(CurrencyConversionDto.class));

        // when
        CurrencyConversionDto result = currencyService.convertCurrency(command);

        // then
        assertEquals(expectedConversion, result);
        verify(exchangeClient).getCurrencyConversion(
                command.getFrom(),
                command.getTo(),
                command.getAmount(),
                command.getDate()
        );
        verify(emailService).send(emailCaptor.capture(), conversionDtoCaptor.capture());
        assertEquals("jankowalksiii12@gmail.com", emailCaptor.getValue());
        assertEquals(expectedConversion, conversionDtoCaptor.getValue());
    }

    @Test
    void testGetFluctuation_ReturnsFluctuationFromClient() {
        // given
        FluctuationCommand command = FluctuationCommand.builder()
                .start_date(LocalDate.parse("2025-04-01"))
                .end_date(LocalDate.parse("2025-05-01"))
                .base("USD")
                .symbols(Collections.singletonList("EUR,GBP,JPY"))
                .build();

        Map<String, BigDecimal> startRates = new HashMap<>();
        startRates.put("EUR", new BigDecimal("0.85"));
        startRates.put("GBP", new BigDecimal("0.75"));
        startRates.put("JPY", new BigDecimal("110.0"));

        Map<String, BigDecimal> endRates = new HashMap<>();
        endRates.put("EUR", new BigDecimal("0.86"));
        endRates.put("GBP", new BigDecimal("0.76"));
        endRates.put("JPY", new BigDecimal("112.0"));

        Map<String, FluctuationDto.FluctuationRate> rates = new HashMap<>();
        rates.put("EUR", FluctuationDto.FluctuationRate.builder()
                .start_rate(new BigDecimal("0.85"))
                .end_rate(new BigDecimal("0.86"))
                .change(new BigDecimal("0.01"))
                .change_pct(new BigDecimal("1.18"))
                .build());
        rates.put("GBP", FluctuationDto.FluctuationRate.builder()
                .start_rate(new BigDecimal("0.75"))
                .end_rate(new BigDecimal("0.76"))
                .change(new BigDecimal("0.01"))
                .change_pct(new BigDecimal("1.33"))
                .build());
        rates.put("JPY", FluctuationDto.FluctuationRate.builder()
                .start_rate(new BigDecimal("110.0"))
                .end_rate(new BigDecimal("112.0"))
                .change(new BigDecimal("2.0"))
                .change_pct(new BigDecimal("1.82"))
                .build());

        FluctuationDto expectedFluctuation = FluctuationDto.builder()
                .base("USD")
                .start_date("2025-04-01")
                .end_date("2025-05-01")
                .rates(rates)
                .success(true)
                .build();

        String expectedSymbols = "EUR,GBP,JPY";

        when(exchangeClient.getFluctuation(
                command.getStart_date(),
                command.getEnd_date(),
                command.getBase(),
                expectedSymbols
        )).thenReturn(expectedFluctuation);

        // when
        FluctuationDto result = currencyService.getFluctuation(command);

        // then
        assertEquals(expectedFluctuation, result);

        verify(exchangeClient).getFluctuation(
                command.getStart_date(),
                command.getEnd_date(),
                command.getBase(),
                expectedSymbols
        );
    }

    @Test
    void testGetLatestRates_ReturnsLatestRatesFromClient() {
        // given
        LatestRatesCommand command = LatestRatesCommand.builder()
                .base("USD")
                .symbols(Collections.singletonList("EUR,GBP,JPY"))
                .build();

        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));
        rates.put("GBP", new BigDecimal("0.75"));
        rates.put("JPY", new BigDecimal("110.0"));

        LatestRatesDto expectedRates = LatestRatesDto.builder()
                .base("USD")
                .date("2025-05-13")
                .timestamp(1683532800000L)
                .rates(rates)
                .success(true)
                .build();

        when(exchangeClient.getLatest(eq(command.getBase()), anyString())).thenReturn(expectedRates);

        // when
        LatestRatesDto result = currencyService.getLatestRates(command);

        // then
        assertEquals(expectedRates, result);

        verify(exchangeClient).getLatest(eq(command.getBase()), anyString());
    }

    @Test
    void testGetTimeSeries_ReturnsTimeSeriesFromClient() {
        // given
        TimeSeriesRatesCommand command = TimeSeriesRatesCommand.builder()
                .startDate(LocalDate.parse("2025-04-01"))
                .endDate(LocalDate.parse("2025-05-01"))
                .base("USD")
                .symbols(Collections.singletonList("EUR,GBP,JPY"))
                .build();

        Map<String, Map<String, BigDecimal>> rates = new HashMap<>();

        Map<String, BigDecimal> apr1Rates = new HashMap<>();
        apr1Rates.put("EUR", new BigDecimal("0.85"));
        apr1Rates.put("GBP", new BigDecimal("0.75"));
        apr1Rates.put("JPY", new BigDecimal("110.0"));
        rates.put("2025-04-01", apr1Rates);

        Map<String, BigDecimal> apr15Rates = new HashMap<>();
        apr15Rates.put("EUR", new BigDecimal("0.86"));
        apr15Rates.put("GBP", new BigDecimal("0.76"));
        apr15Rates.put("JPY", new BigDecimal("111.5"));
        rates.put("2025-04-15", apr15Rates);

        Map<String, BigDecimal> may1Rates = new HashMap<>();
        may1Rates.put("EUR", new BigDecimal("0.87"));
        may1Rates.put("GBP", new BigDecimal("0.77"));
        may1Rates.put("JPY", new BigDecimal("112.0"));
        rates.put("2025-05-01", may1Rates);

        TimeSeriesDto expectedTimeSeries = TimeSeriesDto.builder()
                .base("USD")
                .start_date("2025-04-01")
                .end_date("2025-05-01")
                .rates(rates)
                .success(true)
                .build();

        when(exchangeClient.getTimeSeries(
                eq(command.getStartDate()),
                eq(command.getEndDate()),
                eq(command.getBase()),
                anyString()
        )).thenReturn(expectedTimeSeries);

        // when
        TimeSeriesDto result = currencyService.getTimeSeries(command);

        // then
        assertEquals(expectedTimeSeries, result);
        verify(exchangeClient).getTimeSeries(
                eq(command.getStartDate()),
                eq(command.getEndDate()),
                eq(command.getBase()),
                anyString()
        );
    }

    @Test
    void testGetHistoricalRates_ReturnsHistoricalRatesFromClient() {
        // given
        HistoricalRatesCommand command = HistoricalRatesCommand.builder()
                .date(LocalDate.parse("2025-01-15"))
                .base("USD")
                .symbols(Collections.singletonList("EUR,GBP,JPY"))
                .build();

        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.83"));
        rates.put("GBP", new BigDecimal("0.73"));
        rates.put("JPY", new BigDecimal("105.0"));

        HistoricalRatesDto expectedRates = HistoricalRatesDto.builder()
                .base("USD")
                .date("2025-01-15")
                .timestamp(1673740800000L)
                .rates(rates)
                .success(true)
                .build();

        when(exchangeClient.getHistoricalRates(
                eq(command.getDate()),
                eq(command.getBase()),
                anyString()
        )).thenReturn(expectedRates);

        // when
        HistoricalRatesDto result = currencyService.getHistoricalRates(command);

        // then
        assertEquals(expectedRates, result);

        verify(exchangeClient).getHistoricalRates(
                eq(command.getDate()),
                eq(command.getBase()),
                anyString()
        );
    }
}

