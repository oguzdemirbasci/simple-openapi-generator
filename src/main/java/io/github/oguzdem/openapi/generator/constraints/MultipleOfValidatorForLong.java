package io.github.oguzdem.openapi.generator.constraints;

import io.github.oguzdem.openapi.generator.utils.NumberComparatorHelper;
import java.math.BigDecimal;

/**
 * @author Oguz Demirbasci
 */
public class MultipleOfValidatorForLong extends AbstractMultipleOfValidator<Long> {

  @Override
  public int remainder(Long var) {
    return NumberComparatorHelper.compare(
        new BigDecimal(var).remainder(BigDecimal.valueOf(this.multipleOf)), 0L);
  }
}
