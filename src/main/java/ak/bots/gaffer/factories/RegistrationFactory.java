package ak.bots.gaffer.factories;

import ak.bots.gaffer.domain.MessageId;
import ak.bots.gaffer.domain.Registration;
import ak.bots.gaffer.domain.requests.CancelRegistrationRequest;
import ak.bots.gaffer.domain.requests.RegistrationRequest;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RegistrationFactory {

  private final UserFactory userFactory;

  public RegistrationRequest createRegistrationRequest(CallbackQuery callbackQuery) {

    Message message = callbackQuery.message();
    return RegistrationRequest.builder().messageId(
            new MessageId(message.chat().id(), message.messageId()))
        .user(userFactory.createUser(callbackQuery.from()))
        .build();
  }

  public CancelRegistrationRequest createCancelRegistrationRequest(CallbackQuery callbackQuery) {
    Message message = callbackQuery.message();
    return CancelRegistrationRequest.builder()
        .messageId(new MessageId(message.chat().id(), message.messageId()))
        .user(userFactory.createUser(callbackQuery.from()))
        .build();
  }

  public Registration createRegistration(QueryDocumentSnapshot queryDocumentSnapshot) {
    return Registration.builder()
        .id(UUID.fromString(queryDocumentSnapshot.getId()))
        .user(userFactory.createUser(queryDocumentSnapshot))
        .registrationTime(
            Instant.ofEpochSecond(queryDocumentSnapshot.getLong("registrationTime")))
        .registrationNumber(queryDocumentSnapshot.getLong("registrationNumber"))
        .cancelled(queryDocumentSnapshot.getBoolean("cancelled"))
        .cancellationTime(
            Optional.ofNullable(queryDocumentSnapshot.getLong("cancellationTime"))
                .map(Instant::ofEpochSecond).orElse(null))
        .build();
  }
}
