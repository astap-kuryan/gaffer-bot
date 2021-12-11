package ak.bots.gaffer.processors;

import com.pengrad.telegrambot.model.Update;

public abstract class UpdateProcessor {

  public void process(Update update) {
    if(!isProcessable(update)){
      throw new IllegalArgumentException("Update is not processable by this processor");
    }
    execute(update);
  }

  public abstract boolean isProcessable(Update update);

  protected abstract void execute(Update update);
}
