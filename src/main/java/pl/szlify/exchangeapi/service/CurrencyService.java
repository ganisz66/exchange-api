package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.szlify.exchangeapi.model.TestModel;
import pl.szlify.exchangeapi.model.dto.CurrencyRateDto;
import pl.szlify.exchangeapi.model.dto.FluctuationResponseDto;
import pl.szlify.exchangeapi.properties.ExchangeApiProperties;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final ExchangeApiProperties properties;
    private final RestTemplate restTemplate;

    public TestModel test() {
        return new TestModel(properties.getBaseUrl(), properties.getApiKey());
    }

    public BigDecimal convertCurrency(String from, String to, BigDecimal amount, LocalDate date) {
        URI baseUri = URI.create(properties.getBaseUrl() + "/convert");

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .newInstance()
                .uri(baseUri)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("amount", amount);

        if (date != null) {
            uriBuilder.queryParam("date", date.toString());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", properties.getApiKey());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                uriBuilder.build().toUri(),
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        Object resultObj = Objects.requireNonNull(response.getBody()).get("result");
        return new BigDecimal(resultObj.toString());
    }

    public Map<String, CurrencyRateDto> getFluctuation(LocalDate endDate, LocalDate startDate, String baseCurrency, List<String> symbols) {
        URI baseUri = URI.create(properties.getBaseUrl() + "/fluctuation");

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .newInstance()
                .uri(baseUri)
                .queryParam("end_date", endDate.toString())
                .queryParam("start_date", startDate.toString())
                .queryParam("base", baseCurrency);

        if (symbols != null && !symbols.isEmpty()) {
            String joinedSymbols = String.join(",", symbols);
            uriBuilder.queryParam("symbols", joinedSymbols);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", properties.getApiKey());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<FluctuationResponseDto> response = restTemplate.exchange(
                uriBuilder.build().toUri(),
                HttpMethod.GET,
                requestEntity,
                FluctuationResponseDto.class
        );

        return Objects.requireNonNull(response.getBody()).getRates();
    }



}
