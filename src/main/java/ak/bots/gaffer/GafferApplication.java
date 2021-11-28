package ak.bots.gaffer;


import ak.bots.gaffer.handlers.MessageHandler;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@SpringBootApplication
@Slf4j
public class GafferApplication {

    public static void main(String[] args) {
        SpringApplication.run(GafferApplication.class, args);
    }

    @Bean
    public Consumer<Message<Update>> messageConsumer(@Autowired MessageHandler messageHandler) {
        log.info("Function created");
        return value -> messageHandler.handle(value);
    }

}

