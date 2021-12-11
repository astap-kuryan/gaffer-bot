package ak.bots.gaffer.processors;

import com.pengrad.telegrambot.model.Update;

public abstract class CallbackQueryProcessor extends UpdateProcessor {

  protected boolean isCallbackQuery(Update update) {
    return update.callbackQuery() != null;
  }

}
