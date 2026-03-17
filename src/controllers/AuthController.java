package controllers;


import core.Session;
import services.AuthService;
import models.User;

public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    public boolean login(String username, String password){

        User user = authService.login(username,password);

        if(user != null){
            Session.setCurrentUser(user);
            return true;
        }

        return false;
    }
}