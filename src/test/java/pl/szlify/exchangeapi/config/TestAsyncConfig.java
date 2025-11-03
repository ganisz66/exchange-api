package pl.szlify.exchangeapi.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@TestConfiguration
public class TestAsyncConfig {

    @Bean(name = "asyncTaskExecutor")
    public TaskExecutor taskExecutor() {
        return Runnable::run;
    }
}
