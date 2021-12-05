package ak.bots.gaffer.services;

import ak.bots.gaffer.domain.Event;
import ak.bots.gaffer.domain.MessageId;
import ak.bots.gaffer.domain.repositories.EventRepository;
import ak.bots.gaffer.domain.requests.CancelRegistrationRequest;
import ak.bots.gaffer.domain.requests.EventCreationRequest;
import ak.bots.gaffer.domain.requests.RegistrationRequest;
import ak.bots.gaffer.factories.EventFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

  private final TelegramApiService telegramApiService;
  private final EventRepository eventRepository;
  private final EventFactory eventFactory;

  public void createEvent(EventCreationRequest request) {
    log.info("Creating new event");

    MessageId messageId = telegramApiService.createEventMessage(request);
    Event event = eventFactory.createEvent(request, messageId);
    try {
      eventRepository.save(event);
    } catch (Exception e) {
      log.error("Event {} saving failed", event.getId());
      telegramApiService.deleteEventMessage(event);
    }
  }

  public boolean createRegistration(RegistrationRequest registrationRequest) {
    log.info("Creating new registration");

    Event event = eventRepository.findEvent(registrationRequest.getMessageId());
    boolean registrationAdded = event.addRegistration(registrationRequest);
    if (registrationAdded) {
      eventRepository.save(event);
      telegramApiService.updateEventMessage(event);
    }
    return registrationAdded;
  }

  public boolean cancelRegistration(CancelRegistrationRequest request) {
    log.info("Cancelling registration");

    Event event = eventRepository.findEvent(request.getMessageId());
    boolean registrationCancelled = event.cancelRegistration(request);
    if (registrationCancelled) {
      eventRepository.save(event);
    }
    telegramApiService.updateEventMessage(event);
    return registrationCancelled;
  }
}
