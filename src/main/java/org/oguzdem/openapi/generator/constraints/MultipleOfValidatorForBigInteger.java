package org.oguzdem.json.constraints;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.oguzdem.json.utils.NumberComparatorHelper;

/**
 * @author Oguz Demirbasci
 */
public class MultipleOfValidatorForBigInteger extends AbstractMultipleOfValidator<BigInteger> {

  @Override
  public int remainder(BigInteger var) {
    return NumberComparatorHelper.compare(
        new BigDecimal(var).remainder(BigDecimal.valueOf(this.multipleOf)), 0L);
  }
}
