package org.oguzdem.openapi.generator.constraints;

import java.math.BigDecimal;
import org.oguzdem.openapi.generator.utils.NumberComparatorHelper;

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
