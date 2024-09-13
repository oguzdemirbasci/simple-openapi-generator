package io.github.oguzdem.openapi.generator.constraints;

import io.github.oguzdem.openapi.generator.utils.NumberComparatorHelper;
import java.math.BigDecimal;

/**
 * @author Oguz Demirbasci
 */
public class MultipleOfValidatorForFloat extends AbstractMultipleOfValidator<Float> {

  @Override
  public int remainder(Float var) {
    try {
      BigDecimal convertedValue = new BigDecimal(var.toString());
      return NumberComparatorHelper.compare(
          convertedValue.remainder(BigDecimal.valueOf(this.multipleOf)), 0L);
    } catch (Exception e) {
      return -1;
    }
  }
}
