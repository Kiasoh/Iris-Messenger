package ir.mohaymen.iris.token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class TokenDeleteScheduler implements SchedulingConfigurer {

    private final TokenRepository tokenRepository;

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
                tokenRepository::deleteAllExpiredTokens,
                context -> {
                    Instant lastCompletionTime = Optional.ofNullable(context.lastCompletion()).orElse(Instant.now());
                    return lastCompletionTime.plus(30, ChronoUnit.DAYS);
                });
    }

}