package pl.szlify.exchangeapi.config;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szlify.exchangeapi.client.ExchangeClient;
import pl.szlify.exchangeapi.properties.ExchangeApiProperties;

@Configuration
@RequiredArgsConstructor
public class FeignClientConfig {

    private final ExchangeApiProperties exchangeApiProperties;

    @Bean
    public ExchangeClient exchangeClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(ExchangeClient.class))
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(template -> template.header("apikey", exchangeApiProperties.getApiKey()))
                .target(ExchangeClient.class, exchangeApiProperties.getBaseUrl());
    }
}
