package com.github.oguzdem.openapi.generator.constraints;

import com.github.oguzdem.openapi.generator.utils.NumberComparatorHelper;
import java.math.BigDecimal;

/**
 * @author Oguz Demirbasci
 */
public class MultipleOfValidatorForBigDecimal extends AbstractMultipleOfValidator<BigDecimal> {

  @Override
  public int remainder(BigDecimal var) {
    return NumberComparatorHelper.compare(var.remainder(BigDecimal.valueOf(this.multipleOf)), 0L);
  }
}
