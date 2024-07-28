package org.oguzdem.json.constraints;

import java.math.BigDecimal;
import org.oguzdem.json.utils.NumberComparatorHelper;

/**
 * @author Oguz Demirbasci
 */
public class MultipleOfValidatorForInteger extends AbstractMultipleOfValidator<Integer> {

  @Override
  public int remainder(Integer var) {
    return NumberComparatorHelper.compare(
        new BigDecimal(var).remainder(BigDecimal.valueOf(this.multipleOf)), 0L);
  }
}
