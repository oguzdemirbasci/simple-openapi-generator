package org.oguzdem.openapi.generator.constraints;

import java.math.BigDecimal;
import org.oguzdem.openapi.generator.utils.NumberComparatorHelper;

/**
 * @author Oguz Demirbasci
 */
public class MultipleOfValidatorForNumber extends AbstractMultipleOfValidator<Number> {

  @Override
  public int remainder(Number var) {
    try {
      BigDecimal convertedValue = new BigDecimal(var.toString());
      return NumberComparatorHelper.compare(
          convertedValue.remainder(BigDecimal.valueOf(this.multipleOf)), 0L);
    } catch (Exception e) {
      return -1;
    }
  }
}