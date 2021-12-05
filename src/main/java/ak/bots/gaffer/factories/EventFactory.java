package ak.bots.gaffer.factories;

import ak.bots.gaffer.domain.Event;
import ak.bots.gaffer.domain.MessageId;
import ak.bots.gaffer.domain.Registration;
import ak.bots.gaffer.domain.requests.EventCreationRequest;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.pengrad.telegrambot.model.Message;
import java.time.LocalDateTime;
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
    return EventCreationRequest.builder().chatId(message.chat().id()).build();
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
        .when(document.get("when", LocalDateTime.class))
        .build();
  }

  public Event createEvent(EventCreationRequest request, MessageId messageId) {
    return Event.builder().messageId(messageId).when(request.getWhen()).build();
  }
}
