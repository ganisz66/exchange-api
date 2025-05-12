package pl.szlify.exchangeapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pl.szlify.exchangeapi.properties.AsyncEmailProperties;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class AsyncConfig {

    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor(AsyncEmailProperties asyncEmailProperties) {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(asyncEmailProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncEmailProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncEmailProperties.getQueueCapacity());
        executor.setThreadNamePrefix(asyncEmailProperties.getThreadNamePrefix());
        executor.setWaitForTasksToCompleteOnShutdown(asyncEmailProperties.isWaitForTasksToCompleteOnShutdown());
        executor.setRejectedExecutionHandler(getRejectionPolicy(asyncEmailProperties.getRejectionPolicy()));
        executor.initialize();

        return executor;
    }

    private RejectedExecutionHandler getRejectionPolicy(String policy) {

        if (policy == null) {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        }

        return switch (policy.toUpperCase()) {
            case "ABORT" -> new ThreadPoolExecutor.AbortPolicy();
            case "DISCARD" -> new ThreadPoolExecutor.DiscardPolicy();
            case "DISCARD_OLDEST" -> new ThreadPoolExecutor.DiscardOldestPolicy();
            default -> new ThreadPoolExecutor.CallerRunsPolicy();
        };
    }
}

