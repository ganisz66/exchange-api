package pl.szlify.exchangeapi.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange.async")
@Getter
@Setter
public class AsyncEmailProperties {

    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private String threadNamePrefix;
    private boolean waitForTasksToCompleteOnShutdown;
    private String rejectionPolicy;

}
