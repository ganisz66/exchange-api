package pl.szlify.exchangeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.szlify.exchangeapi.properties.AsyncEmailProperties;
import pl.szlify.exchangeapi.properties.CacheProperties;
import pl.szlify.exchangeapi.properties.ExchangeApiProperties;
import pl.szlify.exchangeapi.properties.SecurityApiProperties;

@SpringBootApplication
@EnableConfigurationProperties({ExchangeApiProperties.class, CacheProperties.class, AsyncEmailProperties.class, SecurityApiProperties.class})
@EnableCaching
@EnableAsync
public class ExchangeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeApiApplication.class, args);
    }
}
