package pl.szlify.exchangeapi.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange.api")
@Getter
@Setter
public class ExchangeApiProperties {

    private String baseUrl;
    private String apiKey;
}
