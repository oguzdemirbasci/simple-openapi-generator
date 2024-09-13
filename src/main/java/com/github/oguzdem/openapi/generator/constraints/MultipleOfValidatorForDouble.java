package com.github.oguzdem.openapi.generator.constraints;

import com.github.oguzdem.openapi.generator.utils.NumberComparatorHelper;
import java.math.BigDecimal;

/**
 * @author Oguz Demirbasci
 */
public class MultipleOfValidatorForDouble extends AbstractMultipleOfValidator<Double> {

  @Override
  public int remainder(Double var) {
    try {
      BigDecimal convertedValue = new BigDecimal(var.toString());
      return NumberComparatorHelper.compare(
          convertedValue.remainder(BigDecimal.valueOf(this.multipleOf)), 0L);
    } catch (Exception e) {
      return -1;
    }
  }
}
