package org.oguzdem.json.constraints;

import java.math.BigDecimal;
import org.oguzdem.json.utils.NumberComparatorHelper;

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
