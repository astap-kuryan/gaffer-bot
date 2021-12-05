package ak.bots.gaffer.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "telegram")
@Validated
public class TelegramProperties {


  private String token;

}
