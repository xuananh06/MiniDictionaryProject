package services;

import repositories.IUserRepository;

import java.util.Optional;

import core.Session;
import models.User;

public class AuthService implements IAuthService {

    private IUserRepository userRepository;

    public AuthService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public boolean login(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            Session.login(user.get());
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        Session.logout();
    }
}
