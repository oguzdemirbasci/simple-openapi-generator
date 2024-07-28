package org.oguzdem.openapi.generator.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element must be a number, and the value must be a multiple of the specified value.
 *
 * @author Oguz Demirbasci
 */
@Target({
  ElementType.METHOD,
  ElementType.FIELD,
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR,
  ElementType.PARAMETER,
  ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MultipleOf.List.class)
@Documented
@Constraint(
    validatedBy = {
      MultipleOfValidatorForBigDecimal.class,
      MultipleOfValidatorForBigInteger.class,
      MultipleOfValidatorForDouble.class,
      MultipleOfValidatorForFloat.class,
      MultipleOfValidatorForInteger.class,
      MultipleOfValidatorForLong.class,
      MultipleOfValidatorForNumber.class,
      MultipleOfValidatorForShort.class
    })
public @interface MultipleOf {

  String message() default "{jakarta.validation.constraints.MultipleOf.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  double value();

  @Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER,
    ElementType.TYPE_USE
  })
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    MultipleOf[] value();
  }
}
