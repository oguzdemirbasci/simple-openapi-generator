package com.github.oguzdem.openapi.generator.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import java.time.OffsetDateTime;
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
class DateTimeFieldGeneratorTest {

  public static Stream<Arguments> generateFieldTestArgsProvider() {
    return Stream.of(
        Arguments.of(
            new DateTimeFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "dateTimeField",
                new DateTimeSchema(),
                false,
                new Components()),
            OffsetDateTime.class));
  }

  @ParameterizedTest
  @MethodSource("generateFieldTestArgsProvider")
  void generateFieldTest(DateTimeFieldGenerator generator, Class<?> expectedFieldType) {
    PropertySource<JavaClassSource> actualField = generator.generateField();
    assertEquals(expectedFieldType.getSimpleName(), actualField.getType().getSimpleName());
  }
}