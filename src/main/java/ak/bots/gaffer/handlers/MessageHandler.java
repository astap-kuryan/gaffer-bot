package ak.bots.gaffer.handlers;

import ak.bots.gaffer.domain.requests.CancelRegistrationRequest;
import ak.bots.gaffer.domain.requests.EventCreationRequest;
import ak.bots.gaffer.domain.requests.RegistrationRequest;
import ak.bots.gaffer.factories.EventFactory;
import ak.bots.gaffer.factories.RegistrationFactory;
import ak.bots.gaffer.services.EventService;
import ak.bots.gaffer.services.TelegramApiService;
import com.pengrad.telegrambot.model.MessageEntity.Type;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MessageHandler {

  private static final String START_COMMAND = "/start";

  private final EventService eventService;
  private final EventFactory eventFactory;
  private final RegistrationFactory registrationFactory;
  private final TelegramApiService telegramApiService;

  public void handle(Message<Update> message) {
    log.info("Update {} received", message.getPayload().updateId());

    Update update = message.getPayload();
    if (isCommand(update)) {
      processCommand(update);
    } else if (isCallback(update)) {
      processCallback(update);
    }
  }

  private void processCallback(Update update) {
    boolean isSuccess = false;
    if (update.callbackQuery().data().equals("+")) {
      RegistrationRequest request = registrationFactory.createRegistrationRequest(
          update.callbackQuery());
      isSuccess = eventService.createRegistration(request);
    } else if (update.callbackQuery().data().equals("-")) {
      CancelRegistrationRequest request = registrationFactory.createCancelRegistrationRequest(
          update.callbackQuery());
      isSuccess = eventService.cancelRegistration(request);
    }

    telegramApiService.replyCallback(update.callbackQuery(), isSuccess);
  }

  private void processCommand(Update update) {
    if (isCreateCommand(update)) {
      EventCreationRequest request = eventFactory.createEvent(update.message());
      eventService.createEvent(request);
    }
  }

  private boolean isCallback(Update update) {
    return update.callbackQuery() != null;
  }

  private boolean isCreateCommand(Update update) {
    return isCommand(update) && update.message() != null && update.message().text().startsWith(
        START_COMMAND);
  }

  private boolean isCommand(Update update) {
    if (update.message() == null && update.editedMessage() == null) {
      return false;
    }
    com.pengrad.telegrambot.model.Message message = ObjectUtils.firstNonNull(update.message(),
        update.editedMessage());

    return message.entities() != null && Arrays.stream(message.entities())
        .anyMatch(messageEntity -> messageEntity.type() == Type.bot_command);
  }
}
