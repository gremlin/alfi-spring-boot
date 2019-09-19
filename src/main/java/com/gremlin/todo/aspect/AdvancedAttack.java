package com.gremlin.todo.aspect;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Attack
public @interface AdvancedAttack {
    String type() default "";
    String[] fields() default {};
}
