package ak.bots.gaffer.services;

import ak.bots.gaffer.domain.Event;
import ak.bots.gaffer.domain.MessageId;
import ak.bots.gaffer.domain.User;
import ak.bots.gaffer.domain.requests.EventCreationRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TelegramApiService {

  private final TelegramBot bot;
  private final MessagePrinter printer;

  public MessageId createEventMessage(EventCreationRequest event) {
    SendMessage request = new SendMessage(event.getChatId(), printer.printMessage(event));
    request.parseMode(ParseMode.HTML);
    request.replyMarkup(getButtons());
    SendResponse response = bot.execute(request);

    if (!response.isOk()) {
      log.error("Response code: {}, description: {}", response.errorCode(), response.description());
      throw new RuntimeException(response.toString());
    }

    return new MessageId(response.message().chat().id(), response.message().messageId());
  }

  public void deleteEventMessage(Event event) {
    DeleteMessage request = new DeleteMessage(event.getMessageId().getChatId(),
        event.getMessageId().getMessageId());
    BaseResponse response = bot.execute(request);

    if (!response.isOk()) {
      log.error("Failed to delete message. Response code: {}, description: {}",
          response.errorCode(), response.description());
    }
  }

  public void updateEventMessage(Event event) {
    String text = printer.printMessage(event);
    EditMessageText request = new EditMessageText(event.getMessageId().getChatId(),
        event.getMessageId().getMessageId(), text);
    request.parseMode(ParseMode.HTML);
    request.replyMarkup(getButtons());
    BaseResponse response = bot.execute(request);
    if (!response.isOk()) {
      log.error("Failed to delete message. Response code: {}, description: {}",
          response.errorCode(), response.description());
    }
  }

  public void replyCallback(CallbackQuery query, String message) {
    AnswerCallbackQuery request = new AnswerCallbackQuery(query.id());
    request.text(message);
    BaseResponse response = bot.execute(request);
    if (!response.isOk()) {
      log.error("Failed to callback. Response code: {}, description: {}",
          response.errorCode(), response.description());
    }
  }

  public void sendNotification(MessageId messageId,
      User user) {
    String text = user.getMention() + ", ты в игре";
    SendMessage request = new SendMessage(messageId.getChatId(), text);
    SendResponse response = bot.execute(request);
    if (!response.isOk()) {
      log.error("Failed to callback. Response code: {}, description: {}",
          response.errorCode(), response.description());
    }

  }

  private InlineKeyboardMarkup getButtons() {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    inlineKeyboardMarkup.addRow(createInlineButton("+"), createInlineButton("-"));
    return inlineKeyboardMarkup;
  }

  private InlineKeyboardButton createInlineButton(String value) {
    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(value);
    inlineKeyboardButton.callbackData(value);
    return inlineKeyboardButton;
  }
}
