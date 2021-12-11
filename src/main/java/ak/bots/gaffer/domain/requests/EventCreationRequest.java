package ak.bots.gaffer.domain.requests;

import ak.bots.gaffer.domain.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class EventCreationRequest {

  private Long chatId;
  private User user;
  private String note;

}
