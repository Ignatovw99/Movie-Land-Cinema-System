package movieland.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@Configuration
@EnableAsync
@EnableScheduling
public class ApplicationBeanConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
