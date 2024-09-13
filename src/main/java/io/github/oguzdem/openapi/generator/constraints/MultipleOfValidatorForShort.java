package io.github.oguzdem.openapi.generator.constraints;

import io.github.oguzdem.openapi.generator.utils.NumberComparatorHelper;
import java.math.BigDecimal;

/**
 * @author Oguz Demirbasci
 */
public class MultipleOfValidatorForShort extends AbstractMultipleOfValidator<Short> {

  @Override
  public int remainder(Short var) {
    return NumberComparatorHelper.compare(
        new BigDecimal(var).remainder(BigDecimal.valueOf(this.multipleOf)), 0L);
  }
}
