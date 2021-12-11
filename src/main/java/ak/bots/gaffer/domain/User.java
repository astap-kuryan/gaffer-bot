package ak.bots.gaffer.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {


  @Include
  private Long id;
  private String firstName;
  private String lastName;
  private String username;

  public String getName() {
    return lastName == null ? firstName : firstName + " " + lastName;
  }

  public String getMention() {
    return "@" + (username == null ? getName() : getUsername());
  }
}
