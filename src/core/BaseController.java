package core;

import annotations.AuthN;
import annotations.AuthZ;
import models.User;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
     BaseController accessible by all Controllers, 
 * Tất cả Controller phải kế thừa từ lớp này.
 */
public abstract class BaseController {

    /**
     * Thực thi một action theo tên, tự động kiểm tra @AuthN và @AuthZ.
     *
     * @param action tên method cần gọi
     * @param args   tham số truyền vào method
     */
    public void execute(String action, Object... args) {
        try {
            Method method = findMethod(this.getClass(), action, args.length);
            if (method == null) {
                System.out.println("[BaseController] Action '" + action + "' not found in "
                        + this.getClass().getSimpleName());
                return;
            }

            // --- Kiểm tra Authentication (@AuthN) ---
            if (method.isAnnotationPresent(AuthN.class)) {
                if (!Session.isLoggedIn()) {
                    System.out.println("[AuthN] You are not logged in. Please login first!");
                    return;
                }
            }

            // --- Kiểm tra Authorization (@AuthZ - RBAC) ---
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

            // --- Thực thi method ---
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
