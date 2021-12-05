package ak.bots.gaffer.factories;

import ak.bots.gaffer.domain.User;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

  User createUser(com.pengrad.telegrambot.model.User user) {
    return User.builder().id(user.id()).username(user.username()).name(user.firstName())
        .build();
  }

  public User createUser(QueryDocumentSnapshot queryDocumentSnapshot) {
    return User.builder()
        .id(queryDocumentSnapshot.get("userId", Long.class))
        .username(queryDocumentSnapshot.get("username", String.class))
        .name(queryDocumentSnapshot.get("name", String.class))
        .build();
  }
}
