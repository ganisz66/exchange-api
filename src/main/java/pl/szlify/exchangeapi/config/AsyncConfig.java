package pl.szlify.exchangeapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean
    public Executor asyncTaskExecutor() {   //TODO: ThreadPoolTaskExecutor
        return Executors.newFixedThreadPool(2); //TODO: dane konfiguracyjne z yml a nie hardcode
    }
}
