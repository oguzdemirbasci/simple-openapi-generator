package com.github.oguzdem.openapi.generator.constraints;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.github.oguzdem.openapi.generator.utils.NumberComparatorHelper;

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
