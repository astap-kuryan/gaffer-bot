package ak.bots.gaffer.processors;

import com.pengrad.telegrambot.model.MessageEntity.Type;
import com.pengrad.telegrambot.model.Update;
import java.util.Arrays;
import org.apache.commons.lang3.ObjectUtils;

public abstract class CommandProcessor extends UpdateProcessor {

  protected boolean isCommand(Update update) {
    if (update.message() == null && update.editedMessage() == null) {
      return false;
    }
    com.pengrad.telegrambot.model.Message message = ObjectUtils.firstNonNull(update.message(),
        update.editedMessage());

    return message.entities() != null && Arrays.stream(message.entities())
        .anyMatch(messageEntity -> messageEntity.type() == Type.bot_command);
  }

}
