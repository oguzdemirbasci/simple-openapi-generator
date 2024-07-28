package org.oguzdem.openapi.generator.constraints;

import java.math.BigDecimal;
import org.oguzdem.openapi.generator.utils.NumberComparatorHelper;

/**
 * @author Oguz Demirbasci
 */
public class MultipleOfValidatorForBigDecimal extends AbstractMultipleOfValidator<BigDecimal> {

  @Override
  public int remainder(BigDecimal var) {
    return NumberComparatorHelper.compare(var.remainder(BigDecimal.valueOf(this.multipleOf)), 0L);
  }
}
