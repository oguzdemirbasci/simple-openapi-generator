package com.github.oguzdem.openapi.generator.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableList;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.util.List;
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
class ArrayFieldGeneratorTest {

  public static Stream<Arguments> generateFieldTestArgsProvider() {
    return Stream.of(
        Arguments.of(
            new ArrayFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "arrayField",
                new ArraySchema().items(new StringSchema()),
                false,
                new Components()),
            ImmutableList.class,
            "String"),
        Arguments.of(
            new ArrayFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "arrayField",
                new ArraySchema().items(new IntegerSchema()),
                false,
                new Components()),
            ImmutableList.class,
            "Integer"),
        Arguments.of(
            new ArrayFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "arrayField",
                new ArraySchema()
                    .items(
                        new ObjectSchema()
                            .$ref(Components.COMPONENTS_SCHEMAS_REF + "ReferencedObject")),
                false,
                new Components()
                    .schemas(
                        Map.of(
                            "ReferencedObject",
                            new ObjectSchema().properties(Map.of("prop", new StringSchema()))))),
            ImmutableList.class,
            "ReferencedObject"),
        Arguments.of(
            new ArrayFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "arrayField",
                new ArraySchema().items(new ArraySchema().items(new IntegerSchema())),
                false,
                new Components()),
            ImmutableList.class,
            "ImmutableList<Integer>"),
        Arguments.of(
            new ArrayFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "arrayField",
                new ArraySchema()
                    .items(
                        new ObjectSchema()
                            .oneOf(
                                List.of(
                                    new ObjectSchema()
                                        .properties(Map.of("prop1", new StringSchema()))
                                        .title("Object1"),
                                    new ObjectSchema()
                                        .properties(Map.of("prop2", new IntegerSchema()))
                                        .title("Object2")))),
                false,
                new Components()),
            ImmutableList.class,
            "ArrayField"),
        Arguments.of(
            new ArrayFieldGenerator(
                Roaster.create(JavaClassSource.class),
                Roaster.create(JavaClassSource.class),
                "arrayField",
                new ArraySchema()
                    .items(
                        new ArraySchema()
                            .items(
                                new ObjectSchema()
                                    .allOf(
                                        List.of(
                                            new ObjectSchema()
                                                .properties(Map.of("prop1", new StringSchema())),
                                            new ObjectSchema()
                                                .properties(
                                                    Map.of("prop2", new IntegerSchema())))))),
                false,
                new Components()),
            ImmutableList.class,
            "ImmutableList<ArrayField3>"));
  }

  @ParameterizedTest
  @MethodSource("generateFieldTestArgsProvider")
  void generateFieldTest(
      ArrayFieldGenerator generator, Class<?> expectedFieldType, String expectedTypeArg) {
    PropertySource<JavaClassSource> actualField = generator.generateField();
    assertEquals("arrayField", actualField.getName());
    assertEquals(expectedFieldType.getSimpleName(), actualField.getType().getName());
    assertEquals(expectedTypeArg, actualField.getType().getTypeArguments().get(0).toString());
  }
}
