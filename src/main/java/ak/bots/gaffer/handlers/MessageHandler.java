package ak.bots.gaffer.handlers;

import ak.bots.gaffer.services.TelegramApiService;
import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MessageHandler {

    private final TelegramApiService telegramApiService;

    public void handle(Message<Update> message) {
        log.info("Update {} received", message.getPayload().updateId());
        Update update = message.getPayload();
        if (isCreateRegistration(update)) {
            telegramApiService.createRegistrationMessage(update.message().chat());
        }

    }

    private boolean isCreateRegistration(Update update) {
        return true;
    }
}
