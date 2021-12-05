package ak.bots.gaffer.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class MessageId {

  @NotNull
  private Long chatId;

  @NotNull
  private Integer messageId;
}
