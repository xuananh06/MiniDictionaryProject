package controllers;


import core.BaseController;
import core.Session;
import services.IAuthService;

public class AuthController extends BaseController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    // Don't need @AuthN - @AuthZ
    public void login(String username, String password) {
        if (Session.isLoggedIn()) {
            System.out.println("Already logged in as: " + Session.getCurrentUser());
            return;
        }
        boolean success = authService.login(username, password);
        if (success) {
            System.out.println("Login successful! Welcome " + Session.getCurrentUser());
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    // Don't need @AuthN - @AuthZ
    public void logout() {
        if (!Session.isLoggedIn()) {
            System.out.println("You are not logged in.");
            return;
        }
        String username = Session.getCurrentUser().getUsername();
        authService.logout();
        System.out.println("Logged out: " + username);
    }
}