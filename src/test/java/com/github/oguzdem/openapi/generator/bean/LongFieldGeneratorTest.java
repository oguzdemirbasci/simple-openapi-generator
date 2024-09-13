package com.github.oguzdem.openapi.generator.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
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
class LongFieldGeneratorTest {

  public static Stream<Arguments> generateFieldTestArgsProvider() {
    return Stream.of(
        Arguments.of(
            new LongFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "longField",
                new IntegerSchema().format("int64"),
                false,
                new Components()),
            Long.class),
        Arguments.of(
            new LongFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "longField",
                new IntegerSchema().format("int64").nullable(false),
                false,
                new Components()),
            long.class),
        Arguments.of(
            new LongFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "longField",
                new NumberSchema().format("int64").nullable(false),
                false,
                new Components()),
            long.class));
  }

  @ParameterizedTest
  @MethodSource("generateFieldTestArgsProvider")
  void generateFieldTest(LongFieldGenerator generator, Class<?> expectedFieldType) {
    PropertySource<JavaClassSource> actualField = generator.generateField();
    assertEquals(expectedFieldType.getSimpleName(), actualField.getType().getSimpleName());
  }
}
