package core;

import models.User;

/**
 * Session class is responsible for managing the current user's session in the application. It allows
 * storing and retrieving the currently logged-in user, which can be used for authentication and
 * authorization purposes throughout the application.
 */

// save user login

public class Session {

    private static User currentUser=null;

    public static void login(User user){
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

}