package pl.szlify.exchangeapi.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange.cache")
@Getter
@Setter
public class CacheProperties {

    private int expirationHours;
    private int maximumSize;
}
