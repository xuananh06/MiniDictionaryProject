package core;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * DIManager - Dependency Injection Container (singleton registry).
 *
 * Three ways to register (called by the caller, e.g. Main):
 *   1. di.register(IFoo.class, new FooImpl())   manual: explicit instance
 *   2. di.register(FooImpl.class)               auto-wire: resolve constructor params from registry
 *   3. di.register("controllers.*")             package scan: auto-wire all concrete classes in package
 *
 * Registration order must follow dependency direction: DAL -> Service -> Controller
 */
public class DIManager {
    private final Map<Class<?>, Object> registry = new HashMap<>();

    // ── 1. Manual registration ─────────────────────────────────────────────────

    /** Register with an explicit pre-built instance. */
    public <T> void register(Class<T> type, T instance) {
        registry.put(type, instance);
    }

    // ── 2. Single-class auto-wire ───────────────────────────────────────────────

    /**
     * Instantiate a concrete class by resolving its constructor parameters from
     * the registry, then register it under its own type AND any interfaces it
     * implements (so it can be resolved by interface later).
     */
    public <T> void register(Class<T> type) {
        T instance = autoWire(type);
        registry.put(type, instance);
        for (Class<?> iface : type.getInterfaces()) {
            registry.putIfAbsent(iface, instance);
        }
    }

    // ── 3. Package-scan auto-wire ───────────────────────────────────────────────

    /**
     * Scan a package for concrete classes and auto-wire each one.
     * Interfaces and abstract classes are skipped automatically.
     *
     * @param packagePattern  e.g. "controllers.*" or "controllers"
     */
    @SuppressWarnings("unchecked")
    public void register(String packagePattern) {
        String pkg = packagePattern.endsWith(".*")
                ? packagePattern.substring(0, packagePattern.length() - 2)
                : packagePattern;
        try {
            List<Class<?>> classes = scanPackage(pkg);
            int count = 0;
            for (Class<?> cls : classes) {
                if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) continue;
                register((Class<Object>) cls);
                count++;
            }
            System.out.println("[DIManager] Scanned '" + pkg + "': registered " + count + " class(es).");
        } catch (Exception e) {
            throw new RuntimeException("[DIManager] Failed to scan package '" + pkg + "': " + e.getMessage(), e);
        }
    }

    // ── Resolve ────────────────────────────────────────────────────────────────

    /** Resolve a dependency by type. Throws RuntimeException if not registered. */
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        Object instance = registry.get(type);
        if (instance == null) {
            throw new RuntimeException("[DIManager] Dependency not found: " + type.getName());
        }
        return (T) instance;
    }

    // ── Internals ──────────────────────────────────────────────────────────────

    /**
     * Pick the constructor with the most parameters, resolve each param type
     * from the registry, then call newInstance().
     */
    @SuppressWarnings("unchecked")
    private <T> T autoWire(Class<T> type) {
        try {
            Constructor<?> ctor = Arrays.stream(type.getDeclaredConstructors())
                    .max(Comparator.comparingInt(Constructor::getParameterCount))
                    .orElseThrow(() -> new RuntimeException("No constructor in " + type.getSimpleName()));

            Class<?>[] paramTypes = ctor.getParameterTypes();
            Object[] params = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                params[i] = registry.get(paramTypes[i]);
                if (params[i] == null) {
                    throw new RuntimeException(
                            "Cannot resolve param[" + i + "] " + paramTypes[i].getSimpleName()
                            + " for " + type.getSimpleName() + " — register it first.");
                }
            }
            ctor.setAccessible(true);
            return (T) ctor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            throw new RuntimeException("[DIManager] Auto-wire failed for " + type.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    /** Return all classes found in the given package directory on the classpath. */
    private List<Class<?>> scanPackage(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) return classes;

        File dir = new File(resource.toURI());
        if (!dir.isDirectory()) return classes;

        File[] files = dir.listFiles((d, name) -> name.endsWith(".class"));
        if (files == null) return classes;

        for (File f : files) {
            String className = packageName + "." + f.getName().replace(".class", "");
            classes.add(Class.forName(className));
        }
        return classes;
    }

}
