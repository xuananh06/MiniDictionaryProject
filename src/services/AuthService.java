package services;

import repositories.IUserRepository;
import models.User;

public class AuthService {

    private IUserRepository userRepository;

    public AuthService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User login(String username, String password){

        return userRepository
                .findByUsernameAndPassword(username,password)
                .orElse(null);
    }
}
