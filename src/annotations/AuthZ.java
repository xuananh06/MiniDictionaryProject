package annotations;

import java.lang.annotation.*;

// Authorization, check roles before access method

@Retention(RetentionPolicy.RUNTIME) // keep runtime for reflection
@Target(ElementType.METHOD) // only method
public @interface AuthZ {
    String roles() default "";
}
