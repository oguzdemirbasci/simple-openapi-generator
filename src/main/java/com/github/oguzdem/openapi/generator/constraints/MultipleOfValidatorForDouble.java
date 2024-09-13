package com.github.oguzdem.openapi.generator.constraints;

import java.math.BigDecimal;
import com.github.oguzdem.openapi.generator.utils.NumberComparatorHelper;

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
