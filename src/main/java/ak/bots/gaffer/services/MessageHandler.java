package ak.bots.gaffer.services;

import ak.bots.gaffer.exceptions.MessageHandlerNotFound;
import ak.bots.gaffer.processors.UpdateProcessor;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MessageHandler {

  private List<UpdateProcessor> updateProcessors;

  public void handle(Message<Update> message) {
    log.info("Update {} received", message.getPayload().updateId());

    Update update = message.getPayload();
    UpdateProcessor processor = updateProcessors.stream()
        .filter(updateProcessor -> updateProcessor.isProcessable(update)).findFirst().orElseThrow(
            MessageHandlerNotFound::new);
    processor.process(update);
  }

}
