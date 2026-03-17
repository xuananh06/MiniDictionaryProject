package repositories;

import models.User;
import java.util.Optional;

public interface IUserRepository {
    Optional<User> findByUsernameAndPassword(String username, String password);
}

