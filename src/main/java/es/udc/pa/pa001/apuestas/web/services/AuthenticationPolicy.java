package es.udc.pa.pa001.apuestas.web.services;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface AuthenticationPolicy.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthenticationPolicy {

  /**
   * Value.
   *
   */
  AuthenticationPolicyType value() default AuthenticationPolicyType.ALL_USERS;
}
