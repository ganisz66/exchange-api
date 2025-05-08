package pl.szlify.exchangeapi.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange.spring.mail")
@Getter
@Setter
public class SpringMailProperties {
    private String username;
}
