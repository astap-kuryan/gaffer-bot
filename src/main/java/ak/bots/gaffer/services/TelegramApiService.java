package ak.bots.gaffer.services;

import ak.bots.gaffer.configuration.TelegramProperties;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TelegramApiService {

    private final TelegramBot bot;

    public void createRegistrationMessage(Chat chat) {
        SendMessage request = new SendMessage(chat.id(), "Registration is open");
        request.replyMarkup(getButtons());
        SendResponse response = bot.execute(request);

        if (!response.isOk()) {
            log.error("Response code: {}, description: {}", response.errorCode(), response.description());
            throw new RuntimeException(response.toString());

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
