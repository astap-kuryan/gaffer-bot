package ak.bots.gaffer.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Registration {

  @Builder.Default
  @Include
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
