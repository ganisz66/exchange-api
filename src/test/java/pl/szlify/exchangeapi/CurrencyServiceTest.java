package pl.szlify.exchangeapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.szlify.exchangeapi.properties.ExchangeApiProperties;
import pl.szlify.exchangeapi.service.CurrencyService;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private ExchangeApiProperties properties;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void convertCurrencyTest_ResultInCurrencyBeingConverted() {
        String from  = "USD";
        String to = "PLN";
        BigDecimal amount = new BigDecimal("100.00");
        LocalDate date = LocalDate.of(2025, 4, 22);
        String baseUrl = "https://api.apilayer.com/exchangerates_data";
        String apiKey = "C5V2F4x6V9EsQ7k54vyPEDcrKA7n4g7h";

        when(properties.getBaseUrl()).thenReturn(baseUrl);
        when(properties.getApiKey()).thenReturn(apiKey);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", 92.55);

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.<ParameterizedTypeReference<Map<String, Object>>>any()
        )).thenReturn(responseEntity);

        // when
        BigDecimal result = currencyService.convertCurrency(from, to, amount, date);

        // then
        assertEquals(new BigDecimal("92.55"), result);
    }
}
