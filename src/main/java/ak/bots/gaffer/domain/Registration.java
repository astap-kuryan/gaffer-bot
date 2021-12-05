package ak.bots.gaffer.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Registration {

  @Builder.Default
  private UUID id = UUID.randomUUID();
  private User user;
  private long registrationNumber;
  @Builder.Default
  private Instant registrationTime = Instant.now();


  private boolean cancelled;
  private Instant cancellationTime;

  public void cancel() {
    if (!cancelled) {
      cancelled = true;
      cancellationTime = Instant.now();
    }
  }
}
