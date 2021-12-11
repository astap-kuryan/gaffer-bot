package ak.bots.gaffer.services;

import ak.bots.gaffer.domain.Event;
import ak.bots.gaffer.domain.MessageId;
import ak.bots.gaffer.domain.Registration;
import ak.bots.gaffer.domain.repositories.EventRepository;
import ak.bots.gaffer.exceptions.DocumentNotFoundException;
import ak.bots.gaffer.exceptions.EventCreationException;
import ak.bots.gaffer.factories.EventFactory;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class FirestoreService implements EventRepository {

  public static final String EVENTS_COLLECTION = "events";
  private final Firestore firestore;
  private EventFactory eventFactory;

  public void save(Event event) {
    log.info("Saving event {}", event.getId());

    WriteBatch batch = firestore.batch();
    DocumentReference docRef = firestore.collection(EVENTS_COLLECTION)
        .document(event.getId().toString());

    batch.set(docRef, eventMap(event));

    CollectionReference collection = firestore.collection(event.getId().toString());
    event.getRegistrations().forEach(registration -> save(registration, collection, batch));

    try {
      ApiFuture<List<WriteResult>> future = batch.commit();
      future.get();
      log.info("Event saved: {}", event);
    } catch (Exception e) {
      log.error("Event save failed", e);
      throw new EventCreationException();
    }
  }

  @Override
  public Event findEvent(MessageId messageId) {
    ApiFuture<QuerySnapshot> future = firestore.collection(EVENTS_COLLECTION)
        .whereEqualTo("chatId", messageId.getChatId())
        .whereEqualTo("messageId", messageId.getMessageId()).get();
    try {
      QueryDocumentSnapshot document = future.get().getDocuments().stream().findFirst()
          .orElseThrow(DocumentNotFoundException::new);
      ApiFuture<QuerySnapshot> registrationsQuery = firestore.collection(document.getId()).get();
      List<QueryDocumentSnapshot> registrations = registrationsQuery.get().getDocuments();
      return eventFactory.createEvent(document, registrations);
    } catch (Exception e) {
      log.error("Event not found", e);
      throw new DocumentNotFoundException();
    }
  }

  private void save(Registration registration,
      CollectionReference collection, WriteBatch batch) {
    DocumentReference document = collection.document(registration.getId().toString());
    batch.set(document, this.registrationMap(registration));
  }

  private Map<String, Object> eventMap(Event event) {
    Map<String, Object> data = new HashMap<>();
    data.put("chatId", event.getMessageId().getChatId());
    data.put("messageId", event.getMessageId().getMessageId());
    data.put("note", event.getNote());
    return data;
  }

  private Map<String, Object> registrationMap(Registration registration) {
    Map<String, Object> data = new HashMap<>();
    data.put("userId", registration.getUser().getId());
    data.put("username", registration.getUser().getUsername());
    data.put("firstName", registration.getUser().getFirstName());
    data.put("lastName", registration.getUser().getFirstName());
    data.put("registrationTime", registration.getRegistrationTime().getEpochSecond());
    data.put("registrationNumber", registration.getRegistrationNumber());
    data.put("cancelled", registration.isCancelled());
    data.put("cancellationTime",
        Optional.ofNullable(registration.getCancellationTime()).map(Instant::getEpochSecond)
            .orElse(null));
    return data;
  }
}
