package core;

import models.User;


// save user login

public class Session {

    private static User currentUser;

    public static void setCurrentUser(User user){
        currentUser = user;
    }

    public static User getCurrentUser(){
        return currentUser;
    }

}