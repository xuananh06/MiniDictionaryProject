package core;

import annotations.AuthN;
import annotations.AuthZ;
import models.User;

import java.lang.reflect.Method;
import java.util.Arrays;

/*
     BaseController accessible by all Controllers, 
     All Controllers must inherit from this class.
 */
public abstract class BaseController {

    /**
     * Execute an action based on the name, automatically checking @AuthN and @AuthZ.
     *
     * @param action the name of the method to call
     * @param args   the arguments to pass to the method
     */

    public void execute(String action, Object... args) {
        try {
            Method method = findMethod(this.getClass(), action, args.length);
            if (method == null) {
                System.out.println("[BaseController] Action '" + action + "' not found in "
                        + this.getClass().getSimpleName());
                return;
            }

            // Check Authentication (@AuthN)
            if (method.isAnnotationPresent(AuthN.class)) {
                if (!Session.isLoggedIn()) {
                    System.out.println("[AuthN] You are not logged in. Please login first!");
                    return;
                }
            }

            // Check Authorization (@AuthZ - RBAC)
            if (method.isAnnotationPresent(AuthZ.class)) {
                AuthZ authZ = method.getAnnotation(AuthZ.class);
                String[] allowedRoles = authZ.roles().split("\\|");
                User user = Session.getCurrentUser();
                boolean hasRole = Arrays.stream(allowedRoles)
                        .map(String::trim)
                        .anyMatch(role -> user.getRoles().contains(role));
                if (!hasRole) {
                    System.out.printf("[AuthZ] Account '%s' is not authorized! Required roles: [%s]%n",
                            user.getUsername(), authZ.roles());
                    return;
                }
            }

            // Execute method 
            method.invoke(this, args);

        } catch (Exception e) {
            System.out.println("[BaseController] Error executing '" + action + "': " + e.getMessage());
        }
    }

    private Method findMethod(Class<?> clazz, String name, int paramCount) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == paramCount) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }
}
