package ak.bots.gaffer.domain;

import ak.bots.gaffer.domain.requests.CancelRegistrationRequest;
import ak.bots.gaffer.domain.requests.RegistrationRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Builder
@Slf4j
public class Event {

  @Builder.Default
  private UUID id = UUID.randomUUID();
  @Valid
  private MessageId messageId;

  @NotNull
  private LocalDateTime when;

  @Builder.Default
  private List<Registration> registrations = new ArrayList<>();

  public boolean addRegistration(RegistrationRequest request) {
    if (registrationAllowed(request.getUser())) {
      Registration registration = Registration.builder()
          .registrationTime(Instant.now())
          .user(request.getUser())
          .registrationNumber(getRegistrationCount(request.getUser()))
          .build();
      registrations.add(registration);
      return true;
    } else {
      log.warn("Registrations for user {} exceeds limit", request.getUser().getId());
      return false;
    }
  }

  public boolean cancelRegistration(CancelRegistrationRequest request) {
    Optional<Registration> lastRegistration = findLastRegistration(request.getUser());
    lastRegistration.ifPresent(Registration::cancel);
    return lastRegistration.isPresent();
  }

  public Optional<Registration> findLastRegistration(User user) {
    return registrations.stream()
        .filter(registration -> registration.getUser().equals(user))
        .filter(registration -> !registration.isCancelled())
        .max(Comparator.comparing(Registration::getRegistrationTime));
  }

  public List<Registration> getParticipants() {
    return registrations.stream()
        .filter(Predicate.not(Registration::isCancelled))
        .sorted(Comparator.comparing(Registration::getRegistrationTime))
        .limit(15)
        .collect(Collectors.toList());
  }

  public List<Registration> getQueue() {
    return registrations.stream()
        .filter(Predicate.not(Registration::isCancelled))
        .sorted(Comparator.comparing(Registration::getRegistrationTime))
        .skip(15)
        .collect(Collectors.toList());
  }

  private boolean registrationAllowed(User user) {
    return getRegistrationCount(user) < 3;
  }

  private int getRegistrationCount(User user) {
    return (int) registrations.stream()
        .filter(registration -> registration.getUser().equals(user))
        .filter(registration -> !registration.isCancelled())
        .count();
  }
}
