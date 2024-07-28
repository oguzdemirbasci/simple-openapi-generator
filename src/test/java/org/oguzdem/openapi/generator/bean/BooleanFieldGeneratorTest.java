package org.oguzdem.openapi.generator.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.BooleanSchema;
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
class BooleanFieldGeneratorTest {

  public static Stream<Arguments> generateFieldTestArgsProvider() {
    return Stream.of(
        Arguments.of(
            new BooleanFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "booleanField",
                new BooleanSchema(),
                false,
                new Components()),
            Boolean.class),
        Arguments.of(
            new BooleanFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "booleanField",
                new BooleanSchema().nullable(false),
                false,
                new Components()),
            boolean.class));
  }

  @ParameterizedTest
  @MethodSource("generateFieldTestArgsProvider")
  void generateFieldTest(BooleanFieldGenerator generator, Class<?> expectedFieldType) {
    PropertySource<JavaClassSource> actualField = generator.generateField();
    assertEquals(expectedFieldType.getSimpleName(), actualField.getType().getSimpleName());
  }
}
