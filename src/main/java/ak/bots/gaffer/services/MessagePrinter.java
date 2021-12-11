package ak.bots.gaffer.services;

import ak.bots.gaffer.domain.Event;
import ak.bots.gaffer.domain.Registration;
import ak.bots.gaffer.domain.requests.EventCreationRequest;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MessagePrinter {

  public String printMessage(Event event) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(printHeader());
    stringBuilder.append(printRegistrations(event));
    stringBuilder.append(printQueue(event));
    return stringBuilder.toString();
  }

  public String printMessage(EventCreationRequest event) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(printHeader());
    return stringBuilder.toString();
  }

  private String printRegistrations(Event event) {
    List<Registration> registrationList = event.getParticipants();
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("<b>Зарегистрировано: %d</b>\n", registrationList.size()));
    builder.append(printRegistrations(registrationList));
    builder.append("\n");
    return builder.toString();
  }

  private String printQueue(Event event) {
    List<Registration> queue = event.getQueue();
    StringBuilder builder = new StringBuilder();
    if (!queue.isEmpty()) {
      builder.append(String.format("<b>В очереди: %d</b>\n", queue.size()));
      builder.append(printRegistrations(queue));
    }

    return builder.toString();
  }

  private String printRegistrations(List<Registration> registrationList) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < registrationList.size(); i++) {
      Registration registration = registrationList.get(i);
      builder.append(String.format("%d. %s", i + 1, registration.getUser().getMention()));
      if (registration.getRegistrationNumber() != 0) {
        builder.append(String.format(" %d-й +", registration.getRegistrationNumber()));
      }
      builder.append("\n");
    }
    return builder.toString();
  }

  private String printHeader() {
    return "<b>Регистрация на игру</b>\n\n";
  }

}
