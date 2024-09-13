package io.github.oguzdem.openapi.generator.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Abstract class for the multiple of validator. The annotated element must be a number, and the
 * value must be a multiple of the specified value.
 *
 * @author Oguz Demirbasci
 */
public abstract class AbstractMultipleOfValidator<T> implements ConstraintValidator<MultipleOf, T> {

  protected double multipleOf;

  @Override
  public void initialize(MultipleOf constraintAnnotation) {
    this.multipleOf = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(T value, ConstraintValidatorContext constraintValidatorContext) {
    boolean isValid;
    if (value == null) {
      isValid = true;
    } else {
      isValid = this.remainder(value) == 0;
    }

    if (!isValid) {
      constraintValidatorContext.disableDefaultConstraintViolation();
      constraintValidatorContext
          .buildConstraintViolationWithTemplate(
              "value must be a multiple of %s".formatted(this.multipleOf))
          .addConstraintViolation();
    }
    return isValid;
  }

  protected abstract int remainder(T var1);
}
