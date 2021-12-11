package ak.bots.gaffer.processors;

import ak.bots.gaffer.domain.requests.CancelRegistrationRequest;
import ak.bots.gaffer.factories.RegistrationFactory;
import ak.bots.gaffer.services.EventService;
import ak.bots.gaffer.services.TelegramApiService;
import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CancelRegistrationProcessor extends CallbackQueryProcessor {

  private static final String CANCEL_REGISTRATION_PAYLOAD = "-";

  private final RegistrationFactory registrationFactory;
  private final TelegramApiService telegramApiService;
  private final EventService eventService;

  @Override
  protected void execute(Update update) {
    CancelRegistrationRequest request = registrationFactory.createCancelRegistrationRequest(
        update.callbackQuery());
    boolean isSuccess = eventService.cancelRegistration(request);
    telegramApiService.replyCallback(update.callbackQuery(), isSuccess ? "Registration cancelled": "Unable to cancel registration");
  }

  @Override
  public boolean isProcessable(Update update) {
    return isCallbackQuery(update) && isCancelRegistration(update);
  }

  private boolean isCancelRegistration(Update update) {
    return update.callbackQuery().data().equals(CANCEL_REGISTRATION_PAYLOAD);

  }
}
