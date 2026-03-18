package annotations;

import java.lang.annotation.*;

// Authentication, check login before access method

@Retention(RetentionPolicy.RUNTIME) // keep runtime for reflection
@Target(ElementType.METHOD) // only method
public @interface AuthN {
}
