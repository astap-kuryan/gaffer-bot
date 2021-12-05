package ak.bots.gaffer.domain.requests;

import ak.bots.gaffer.domain.MessageId;
import ak.bots.gaffer.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistrationRequest {

  private MessageId messageId;
  private final User user;

}
