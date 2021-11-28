package ak.bots.gaffer.configuration;

import com.pengrad.telegrambot.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationPropertiesScan
@AllArgsConstructor
@Slf4j
public class Config {


    private final TelegramProperties properties;

    @Bean
    public TelegramBot telegramBot() {
        log.info("Token value: {}", properties.getToken());
        return new TelegramBot(properties.getToken());

    }
}
