package ak.bots.gaffer.domain.repositories;

import ak.bots.gaffer.domain.Event;
import ak.bots.gaffer.domain.MessageId;

public interface EventRepository {

  void save(Event event);

  Event findEvent(MessageId messageId);
}
