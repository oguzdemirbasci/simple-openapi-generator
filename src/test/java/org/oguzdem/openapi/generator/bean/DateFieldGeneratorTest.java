package org.oguzdem.openapi.generator.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.DateSchema;
import java.util.Date;
import java.util.stream.Stream;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Oguz Demirbasci
 */
class DateFieldGeneratorTest {

  public static Stream<Arguments> generateFieldTestArgsProvider() {
    return Stream.of(
        Arguments.of(
            new DateFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "dateField",
                new DateSchema(),
                false,
                new Components()),
            Date.class));
  }

  @ParameterizedTest
  @MethodSource("generateFieldTestArgsProvider")
  void generateFieldTest(DateFieldGenerator generator, Class<?> expectedFieldType) {
    PropertySource<JavaClassSource> actualField = generator.generateField();
    assertEquals(expectedFieldType.getSimpleName(), actualField.getType().getSimpleName());
  }
}
