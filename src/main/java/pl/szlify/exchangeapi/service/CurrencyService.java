package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.szlify.exchangeapi.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.dto.CurrencySymbolsDto;
import pl.szlify.exchangeapi.dto.FluctuationDto;
import pl.szlify.exchangeapi.dto.LatestRatesDto;
import pl.szlify.exchangeapi.model.TestModel;
import pl.szlify.exchangeapi.properties.ExchangeApiProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final ExchangeApiProperties properties;

    public TestModel getTestModel() {
        return new TestModel(properties.getBaseUrl(), properties.getApiKey());
    }


    public CurrencySymbolsDto getAllCurrencySymbols() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", properties.getApiKey());

        ResponseEntity<CurrencySymbolsDto> response = new RestTemplate().exchange(
                properties.getBaseUrl() + "/symbols",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CurrencySymbolsDto.class
        );
        return response.getBody();
    }

    public CurrencyConversionDto convertCurrency(String from, String to, BigDecimal amount, LocalDate date) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(properties.getBaseUrl() + "/convert")
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("amount", amount);

        if (date != null) {
            builder.queryParam("date", date.toString());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", properties.getApiKey());

        ResponseEntity<CurrencyConversionDto> response = new RestTemplate().exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CurrencyConversionDto.class
        );
        return response.getBody();
    }

    public FluctuationDto getFluctuation(LocalDate startDate, LocalDate endDate, String base, String symbols) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(properties.getBaseUrl() + "/fluctuation")
                .queryParam("start_date", startDate.toString())
                .queryParam("end_date", endDate.toString());

        if (base != null && !base.isEmpty()) {
            builder.queryParam("base", base);
        }

        if (symbols != null && !symbols.isEmpty()) {
            builder.queryParam("symbols", symbols);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", properties.getApiKey());

        ResponseEntity<FluctuationDto> response = new RestTemplate().exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                FluctuationDto.class
        );
        return response.getBody();
    }

    public LatestRatesDto getLatestRates(String base, String symbols) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(properties.getBaseUrl() + "/latest");

        if (base != null && !base.isEmpty()) {
            builder.queryParam("base", base);
        }

        if (symbols != null && !symbols.isEmpty()) {
            builder.queryParam("symbols", symbols);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", properties.getApiKey());

        ResponseEntity<LatestRatesDto> response = new RestTemplate().exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                LatestRatesDto.class
        );
        return response.getBody();
    }
}



