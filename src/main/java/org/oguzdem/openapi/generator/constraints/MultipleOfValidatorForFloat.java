package org.oguzdem.json.constraints;

import java.math.BigDecimal;
import org.oguzdem.json.utils.NumberComparatorHelper;

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
