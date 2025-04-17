package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.szlify.exchangeapi.model.TestModel;
import pl.szlify.exchangeapi.properties.ExchangeApiProperties;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final ExchangeApiProperties properties;

    public TestModel test() {
        return new TestModel(properties.getBaseUrl(), properties.getApiKey());
    }
}
