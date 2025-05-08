package pl.szlify.exchangeapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SupportedCurrencyValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotNull
public @interface SupportedCurrency {

    String message() default "CURRENCY_NOT_SUPPORTED";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
