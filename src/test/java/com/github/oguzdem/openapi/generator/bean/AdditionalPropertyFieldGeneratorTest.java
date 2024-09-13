package com.github.oguzdem.openapi.generator.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.util.Map;
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
class AdditionalPropertyFieldGeneratorTest {

  public static Stream<Arguments> generateFieldTestArgsProvider() {
    return Stream.of(
        Arguments.of(
            new AdditionalPropertyFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                new ObjectSchema().additionalProperties(true),
                new Components()),
            ImmutableMap.class,
            "Object"),
        Arguments.of(
            new AdditionalPropertyFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                new ObjectSchema().additionalProperties(new ObjectSchema()),
                new Components()),
            ImmutableMap.class,
            "Object"),
        Arguments.of(
            new AdditionalPropertyFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                new ObjectSchema()
                    .additionalProperties(
                        new ObjectSchema()
                            .$ref(Components.COMPONENTS_SCHEMAS_REF + "ReferencedObject")),
                new Components()
                    .schemas(
                        Map.of(
                            "ReferencedObject",
                            new ObjectSchema().properties(Map.of("prop", new StringSchema()))))),
            ImmutableMap.class,
            "ReferencedObject"),
        Arguments.of(
            new AdditionalPropertyFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                new ObjectSchema().additionalProperties(new StringSchema()),
                new Components()),
            ImmutableMap.class,
            "String"),
        Arguments.of(
            new AdditionalPropertyFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                new ObjectSchema().additionalProperties(new IntegerSchema()),
                new Components()),
            ImmutableMap.class,
            "Integer"),
        Arguments.of(
            new AdditionalPropertyFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                new ObjectSchema().additionalProperties(new ArraySchema()),
                new Components()),
            ImmutableMap.class,
            "ImmutableList<Object>"),
        Arguments.of(
            new AdditionalPropertyFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                new ObjectSchema()
                    .additionalProperties(
                        new ObjectSchema().properties(Map.of("custom Prop", new StringSchema()))),
                new Components()),
            ImmutableMap.class,
            "JavaClassAdditionalProperties"));
  }

  @ParameterizedTest
  @MethodSource("generateFieldTestArgsProvider")
  void generateFieldTest(
      AdditionalPropertyFieldGenerator generator,
      Class<?> expectedFieldType,
      String expectedTypeArg) {
    PropertySource<JavaClassSource> actualField = generator.generateField();
    assertEquals(
        AdditionalPropertyFieldGenerator.ADDITIONAL_PROPERTIES_FIELD_NAME, actualField.getName());
    assertEquals(
        AdditionalPropertyFieldGenerator.ADDITIONAL_PROPERTIES_GETTER_NAME,
        actualField.getAccessor().getName());
    assertEquals(expectedFieldType.getSimpleName(), actualField.getType().getName());
    assertEquals(expectedTypeArg, actualField.getType().getTypeArguments().get(1).toString());
  }
}
