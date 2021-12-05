package ak.bots.gaffer.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.pengrad.telegrambot.TelegramBot;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConfigurationPropertiesScan
@AllArgsConstructor
@Slf4j
public class Config {


  private final Environment env;
  private final TelegramProperties properties;

  @Bean
  public TelegramBot telegramBot() {
    log.info("Token value: {}", properties.getToken());
    return new TelegramBot(properties.getToken());

  }

  @Bean
  public Firestore fireStore() throws IOException {
    GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
    FirestoreOptions options = FirestoreOptions.getDefaultInstance().toBuilder()
        .setCredentials(credentials)
        .setProjectId("gaffer-bot")
        .build();
    return options.getService();
  }
}
