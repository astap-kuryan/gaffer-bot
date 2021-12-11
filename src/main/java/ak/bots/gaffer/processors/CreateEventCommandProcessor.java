package ak.bots.gaffer.processors;

import ak.bots.gaffer.domain.requests.EventCreationRequest;
import ak.bots.gaffer.exceptions.IllegalOperationException;
import ak.bots.gaffer.factories.EventFactory;
import ak.bots.gaffer.services.EventService;
import ak.bots.gaffer.services.TelegramApiService;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.ChatMember.Status;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CreateEventCommandProcessor extends CommandProcessor {

  private static final String START_COMMAND = "/start";

  private final EventService eventService;
  private final EventFactory eventFactory;
  private final TelegramApiService telegramApiService;

  @Override
  protected void execute(Update update) {
    EventCreationRequest request = eventFactory.createEvent(update.message());
    eventService.createEvent(request);
  }

  @Override
  public boolean isProcessable(Update update) {
    return isCommand(update) && isStartCommand(update);
  }

  private boolean isStartCommand(Update update) {
    return update.message() != null && update.message().text().startsWith(
        START_COMMAND);
  }
}
