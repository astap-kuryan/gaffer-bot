package ak.bots.gaffer.factories;

import ak.bots.gaffer.domain.Event;
import ak.bots.gaffer.domain.MessageId;
import ak.bots.gaffer.domain.Registration;
import ak.bots.gaffer.domain.requests.EventCreationRequest;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventFactory {

  private final RegistrationFactory registrationFactory;

  public EventCreationRequest createEvent(Message message) {
    return EventCreationRequest.builder().chatId(message.chat().id()).note(getNote(message))
        .build();
  }

  private String getNote(Message message) {
    Integer commandLength = Arrays.stream(message.entities())
        .filter(messageEntity -> messageEntity.type() == Type.bot_command).findFirst().get()
        .length();
    return message.text().substring(commandLength).trim();
  }

  public Event createEvent(QueryDocumentSnapshot document,
      List<QueryDocumentSnapshot> registrationDocuments) {
    Long chatId = document.get("chatId", Long.class);
    Integer messageId = document.get("messageId", Integer.class);
    List<Registration> registrations = registrationDocuments.stream()
        .map(registrationFactory::createRegistration)
        .collect(Collectors.toList());

    return Event.builder()
        .id(UUID.fromString(document.getId()))
        .messageId(new MessageId(chatId, messageId))
        .registrations(registrations)
        .note(document.getString("note"))
        .build();
  }

  public Event createEvent(EventCreationRequest request, MessageId messageId) {
    return Event.builder().messageId(messageId).note(request.getNote()).build();
  }
}
