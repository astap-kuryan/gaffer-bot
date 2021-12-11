package ak.bots.gaffer.processors;

import ak.bots.gaffer.domain.requests.RegistrationRequest;
import ak.bots.gaffer.factories.RegistrationFactory;
import ak.bots.gaffer.services.EventService;
import ak.bots.gaffer.services.TelegramApiService;
import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NewRegistrationProcessor extends CallbackQueryProcessor {

  private static final String NEW_REGISTRATION_PAYLOAD = "+";

  private final RegistrationFactory registrationFactory;
  private final EventService eventService;
  private final TelegramApiService telegramApiService;

  @Override
  protected void execute(Update update) {
    RegistrationRequest request = registrationFactory.createRegistrationRequest(
        update.callbackQuery());
    boolean isSuccess = eventService.createRegistration(request);
    telegramApiService.replyCallback(update.callbackQuery(), isSuccess ? "Registration added": "Registration failed");
  }

  @Override
  public boolean isProcessable(Update update) {
    return isCallbackQuery(update) && isNewRegistration(update);
  }

  private boolean isNewRegistration(Update update) {
    return update.callbackQuery().data().equals(NEW_REGISTRATION_PAYLOAD);
  }
}
