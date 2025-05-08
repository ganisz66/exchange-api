package pl.szlify.exchangeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import pl.szlify.exchangeapi.properties.CacheProperties;
import pl.szlify.exchangeapi.properties.ExchangeApiProperties;

@SpringBootApplication
@EnableConfigurationProperties({ExchangeApiProperties.class, CacheProperties.class})
@EnableCaching
public class ExchangeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeApiApplication.class, args);
    }
}
