package io.github.oguzdem.openapi.generator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Named;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Oguz Demirbasci
 */
class JavaClassSourceUtilsTest {

  public static Stream<Arguments> isRefTestArgProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(new StringSchema(), false),
        Arguments.of(new IntegerSchema(), false),
        Arguments.of(new NumberSchema(), false),
        Arguments.of(new ObjectSchema(), false),
        Arguments.of(new ArraySchema(), false),
        Arguments.of(new BooleanSchema(), false),
        Arguments.of(new ByteArraySchema(), false),
        Arguments.of(new BinarySchema(), false),
        Arguments.of(new DateSchema(), false),
        Arguments.of(new DateTimeSchema(), false),
        Arguments.of(new UUIDSchema(), false),
        Arguments.of(new MapSchema(), false),
        Arguments.of(new EmailSchema(), false),
        Arguments.of(new ObjectSchema().allOf(List.of()), false),
        Arguments.of(new ObjectSchema().allOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().anyOf(List.of()), false),
        Arguments.of(new ObjectSchema().anyOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().oneOf(List.of()), false),
        Arguments.of(new ObjectSchema().oneOf(List.of(new StringSchema())), false),
        Arguments.of(
            new ObjectSchema().$ref(Components.COMPONENTS_SCHEMAS_REF + "referenceObject"), true));
  }

  public static Stream<Arguments> isJavaObjectTestArgProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(new StringSchema(), false),
        Arguments.of(new IntegerSchema(), false),
        Arguments.of(new NumberSchema(), false),
        Arguments.of(new ObjectSchema(), true),
        Arguments.of(new ArraySchema(), false),
        Arguments.of(new BooleanSchema(), false),
        Arguments.of(new ByteArraySchema(), false),
        Arguments.of(new BinarySchema(), false),
        Arguments.of(new DateSchema(), false),
        Arguments.of(new DateTimeSchema(), false),
        Arguments.of(new UUIDSchema(), false),
        Arguments.of(new MapSchema(), true),
        Arguments.of(new EmailSchema(), false),
        Arguments.of(new ObjectSchema().allOf(List.of()), true),
        Arguments.of(new ObjectSchema().allOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().anyOf(List.of()), true),
        Arguments.of(new ObjectSchema().anyOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().oneOf(List.of()), true),
        Arguments.of(new ObjectSchema().oneOf(List.of(new StringSchema())), false),
        Arguments.of(
            new ObjectSchema().$ref(Components.COMPONENTS_SCHEMAS_REF + "referenceObject"), false));
  }

  public static Stream<Arguments> isObjectTestArgProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(new StringSchema(), false),
        Arguments.of(new IntegerSchema(), false),
        Arguments.of(new NumberSchema(), false),
        Arguments.of(new ObjectSchema(), true),
        Arguments.of(new ArraySchema(), false),
        Arguments.of(new BooleanSchema(), false),
        Arguments.of(new ByteArraySchema(), false),
        Arguments.of(new BinarySchema(), false),
        Arguments.of(new DateSchema(), false),
        Arguments.of(new DateTimeSchema(), false),
        Arguments.of(new UUIDSchema(), false),
        Arguments.of(new MapSchema(), true),
        Arguments.of(new EmailSchema(), false),
        Arguments.of(new ObjectSchema().allOf(List.of()), true),
        Arguments.of(new ObjectSchema().allOf(List.of(new StringSchema())), true),
        Arguments.of(new ObjectSchema().anyOf(List.of()), true),
        Arguments.of(new ObjectSchema().anyOf(List.of(new StringSchema())), true),
        Arguments.of(new ObjectSchema().oneOf(List.of()), true),
        Arguments.of(new ObjectSchema().oneOf(List.of(new StringSchema())), true),
        Arguments.of(
            new ObjectSchema().$ref(Components.COMPONENTS_SCHEMAS_REF + "referenceObject"), true));
  }

  public static Stream<Arguments> isEnumTestArgProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(new StringSchema(), false),
        Arguments.of(new IntegerSchema(), false),
        Arguments.of(new NumberSchema(), false),
        Arguments.of(new ObjectSchema(), false),
        Arguments.of(new ArraySchema(), false),
        Arguments.of(new BooleanSchema(), false),
        Arguments.of(new ByteArraySchema(), false),
        Arguments.of(new BinarySchema(), false),
        Arguments.of(new DateSchema(), false),
        Arguments.of(new DateTimeSchema(), false),
        Arguments.of(new UUIDSchema(), false),
        Arguments.of(new MapSchema(), false),
        Arguments.of(new EmailSchema(), false),
        Arguments.of(new ObjectSchema().allOf(List.of()), false),
        Arguments.of(new ObjectSchema().allOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().anyOf(List.of()), false),
        Arguments.of(new ObjectSchema().anyOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().oneOf(List.of()), false),
        Arguments.of(new ObjectSchema().oneOf(List.of(new StringSchema())), false),
        Arguments.of(
            new ObjectSchema().$ref(Components.COMPONENTS_SCHEMAS_REF + "referenceObject"), false),
        Arguments.of(new StringSchema()._enum(List.of()), false),
        Arguments.of(new StringSchema()._enum(List.of("A", "B")), true));
  }

  public static Stream<Arguments> isArrayTestArgProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(new StringSchema(), false),
        Arguments.of(new IntegerSchema(), false),
        Arguments.of(new NumberSchema(), false),
        Arguments.of(new ObjectSchema(), false),
        Arguments.of(new ArraySchema(), true),
        Arguments.of(new BooleanSchema(), false),
        Arguments.of(new ByteArraySchema(), false),
        Arguments.of(new BinarySchema(), false),
        Arguments.of(new DateSchema(), false),
        Arguments.of(new DateTimeSchema(), false),
        Arguments.of(new UUIDSchema(), false),
        Arguments.of(new MapSchema(), false),
        Arguments.of(new EmailSchema(), false),
        Arguments.of(new ObjectSchema().allOf(List.of()), false),
        Arguments.of(new ObjectSchema().allOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().anyOf(List.of()), false),
        Arguments.of(new ObjectSchema().anyOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().oneOf(List.of()), false),
        Arguments.of(new ObjectSchema().oneOf(List.of(new StringSchema())), false),
        Arguments.of(
            new ObjectSchema().$ref(Components.COMPONENTS_SCHEMAS_REF + "referenceObject"), false),
        Arguments.of(new StringSchema()._enum(List.of()), false),
        Arguments.of(new StringSchema()._enum(List.of("A", "B")), false));
  }

  public static Stream<Arguments> isAllOfTestArgProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(new StringSchema(), false),
        Arguments.of(new IntegerSchema(), false),
        Arguments.of(new NumberSchema(), false),
        Arguments.of(new ObjectSchema(), false),
        Arguments.of(new ArraySchema(), false),
        Arguments.of(new BooleanSchema(), false),
        Arguments.of(new ByteArraySchema(), false),
        Arguments.of(new BinarySchema(), false),
        Arguments.of(new DateSchema(), false),
        Arguments.of(new DateTimeSchema(), false),
        Arguments.of(new UUIDSchema(), false),
        Arguments.of(new MapSchema(), false),
        Arguments.of(new EmailSchema(), false),
        Arguments.of(new ObjectSchema().allOf(List.of()), false),
        Arguments.of(new ObjectSchema().allOf(List.of(new StringSchema())), true),
        Arguments.of(new ObjectSchema().anyOf(List.of()), false),
        Arguments.of(new ObjectSchema().anyOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().oneOf(List.of()), false),
        Arguments.of(new ObjectSchema().oneOf(List.of(new StringSchema())), false),
        Arguments.of(
            new ObjectSchema().$ref(Components.COMPONENTS_SCHEMAS_REF + "referenceObject"), false),
        Arguments.of(new StringSchema()._enum(List.of()), false),
        Arguments.of(new StringSchema()._enum(List.of("A", "B")), false));
  }

  public static Stream<Arguments> isOneOfTestArgProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(new StringSchema(), false),
        Arguments.of(new IntegerSchema(), false),
        Arguments.of(new NumberSchema(), false),
        Arguments.of(new ObjectSchema(), false),
        Arguments.of(new ArraySchema(), false),
        Arguments.of(new BooleanSchema(), false),
        Arguments.of(new ByteArraySchema(), false),
        Arguments.of(new BinarySchema(), false),
        Arguments.of(new DateSchema(), false),
        Arguments.of(new DateTimeSchema(), false),
        Arguments.of(new UUIDSchema(), false),
        Arguments.of(new MapSchema(), false),
        Arguments.of(new EmailSchema(), false),
        Arguments.of(new ObjectSchema().allOf(List.of()), false),
        Arguments.of(new ObjectSchema().allOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().anyOf(List.of()), false),
        Arguments.of(new ObjectSchema().anyOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().oneOf(List.of()), false),
        Arguments.of(new ObjectSchema().oneOf(List.of(new StringSchema())), true),
        Arguments.of(
            new ObjectSchema().$ref(Components.COMPONENTS_SCHEMAS_REF + "referenceObject"), false),
        Arguments.of(new StringSchema()._enum(List.of()), false),
        Arguments.of(new StringSchema()._enum(List.of("A", "B")), false));
  }

  public static Stream<Arguments> isAnyOfTestArgProvider() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of(new StringSchema(), false),
        Arguments.of(new IntegerSchema(), false),
        Arguments.of(new NumberSchema(), false),
        Arguments.of(new ObjectSchema(), false),
        Arguments.of(new ArraySchema(), false),
        Arguments.of(new BooleanSchema(), false),
        Arguments.of(new ByteArraySchema(), false),
        Arguments.of(new BinarySchema(), false),
        Arguments.of(new DateSchema(), false),
        Arguments.of(new DateTimeSchema(), false),
        Arguments.of(new UUIDSchema(), false),
        Arguments.of(new MapSchema(), false),
        Arguments.of(new EmailSchema(), false),
        Arguments.of(new ObjectSchema().allOf(List.of()), false),
        Arguments.of(new ObjectSchema().allOf(List.of(new StringSchema())), false),
        Arguments.of(new ObjectSchema().anyOf(List.of()), false),
        Arguments.of(new ObjectSchema().anyOf(List.of(new StringSchema())), true),
        Arguments.of(new ObjectSchema().oneOf(List.of()), false),
        Arguments.of(new ObjectSchema().oneOf(List.of(new StringSchema())), false),
        Arguments.of(
            new ObjectSchema().$ref(Components.COMPONENTS_SCHEMAS_REF + "referenceObject"), false),
        Arguments.of(new StringSchema()._enum(List.of()), false),
        Arguments.of(new StringSchema()._enum(List.of("A", "B")), false));
  }

  public static Stream<Arguments> fillJavaClassSourceBySchemaTestArgProvider() {
    Schema<?> schema =
        new ObjectSchema()
            .properties(Map.of("prop", new StringSchema()))
            .additionalProperties(true);
    JavaClassSource expected = Roaster.create(JavaClassSource.class);
    expected.addProperty(String.class, "prop");
    expected.addProperty(ImmutableMap.class, "additionalProperties");
    expected.addMethod().setConstructor(true);

    Schema<?> schema1 = new ObjectSchema().properties(Map.of("prop", new StringSchema()));
    JavaClassSource expected1 = Roaster.create(JavaClassSource.class);
    expected1.addProperty(String.class, "prop");

    return Stream.of(
        Arguments.of(
            Roaster.create(JavaClassSource.class),
            Roaster.create(JavaClassSource.class),
            schema,
            new Components(),
            expected),
        Arguments.of(
            Roaster.create(JavaClassSource.class),
            Roaster.create(JavaClassSource.class),
            schema1,
            new Components(),
            expected1));
  }

  public static Stream<Arguments> fillEnumSourceBySchemaTestArgProvider() {
    JavaEnumSource javaEnum = Roaster.create(JavaEnumSource.class);
    Schema<?> schema = new StringSchema()._enum(List.of("A", "B"));
    JavaEnumSource expected = Roaster.create(JavaEnumSource.class);
    expected.addEnumConstant("A");
    expected.addEnumConstant("B");
    return Stream.of(
        Arguments.of(
            Roaster.create(JavaEnumSource.class),
            new StringSchema(),
            Roaster.create(JavaEnumSource.class)),
        Arguments.of(
            Roaster.create(JavaEnumSource.class),
            new StringSchema()._enum(List.of()),
            Roaster.create(JavaEnumSource.class)),
        Arguments.of(javaEnum, schema, expected));
  }

  public static Stream<Arguments> getConstructorTestArgProvider() {
    JavaClassSource clsSourceWithConstructor = Roaster.create(JavaClassSource.class);
    org.jboss.forge.roaster.model.source.MethodSource<JavaClassSource> constructor =
        clsSourceWithConstructor.addMethod().setConstructor(true);
    return Stream.of(
        Arguments.of(null, null),
        Arguments.of(Roaster.create(JavaClassSource.class), null),
        Arguments.of(clsSourceWithConstructor, constructor));
  }

  public static Stream<Arguments> copyPropsTestArgProvider() {
    JavaClassSource javaSource = Roaster.create(JavaClassSource.class);
    javaSource.addProperty(String.class, "value");
    JavaClassSource target = Roaster.create(JavaClassSource.class);
    target.addProperty(Integer.class, "targetField");
    JavaClassSource expected = Roaster.create(JavaClassSource.class);
    expected.addProperty(Integer.class, "targetField");
    expected.addProperty(String.class, "value");
    return Stream.of(
        Arguments.of(null, null, null),
        Arguments.of(javaSource, Roaster.create(JavaClassSource.class), javaSource),
        Arguments.of(javaSource, target, expected));
  }

  @ParameterizedTest
  @MethodSource("isRefTestArgProvider")
  void isRefTest(Schema<?> schema, boolean expected) {
    assertEquals(expected, JavaClassSourceUtils.isRef(schema));
  }

  @ParameterizedTest
  @MethodSource("isJavaObjectTestArgProvider")
  void isJavaObjectTest(Schema<?> schema, boolean expected) {
    assertEquals(expected, JavaClassSourceUtils.isJavaObject(schema));
  }

  @ParameterizedTest
  @MethodSource("isObjectTestArgProvider")
  void isObjectTest(Schema<?> schema, boolean expected) {
    assertEquals(expected, JavaClassSourceUtils.isObject(schema));
  }

  @ParameterizedTest
  @MethodSource("isEnumTestArgProvider")
  void isEnumTest(Schema<?> schema, boolean expected) {
    assertEquals(expected, JavaClassSourceUtils.isEnum(schema));
  }

  @ParameterizedTest
  @MethodSource("isArrayTestArgProvider")
  void isArrayTest(Schema<?> schema, boolean expected) {
    assertEquals(expected, JavaClassSourceUtils.isArray(schema));
  }

  @ParameterizedTest
  @MethodSource("isAllOfTestArgProvider")
  void isAllOfTest(Schema<?> schema, boolean expected) {
    assertEquals(expected, JavaClassSourceUtils.isAllOf(schema));
  }

  @ParameterizedTest
  @MethodSource("isOneOfTestArgProvider")
  void isOneOf(Schema<?> schema, boolean expected) {
    assertEquals(expected, JavaClassSourceUtils.isOneOf(schema));
  }

  @ParameterizedTest
  @MethodSource("isAnyOfTestArgProvider")
  void isAnyOf(Schema<?> schema, boolean expected) {
    assertEquals(expected, JavaClassSourceUtils.isAnyOf(schema));
  }

  @ParameterizedTest
  @MethodSource("fillJavaClassSourceBySchemaTestArgProvider")
  void fillJavaClassSourceBySchemaTest(
      JavaClassSource rootClassSource,
      JavaClassSource classSource,
      Schema<?> schema,
      Components components,
      JavaClassSource expected) {
    JavaClassSourceUtils.fillJavaClassSourceBySchema(
        rootClassSource, classSource, schema, components);
    assertEquals(expected.getFields().size(), classSource.getFields().size());
  }

  @ParameterizedTest
  @MethodSource("fillEnumSourceBySchemaTestArgProvider")
  void fillEnumSourceBySchemaTest(
      JavaEnumSource javaEnum, Schema<?> schema, JavaEnumSource expected) {
    JavaClassSourceUtils.fillEnumSourceBySchema(javaEnum, schema);
    assertEquals(expected.getEnumConstants().size(), javaEnum.getEnumConstants().size());
  }

  @ParameterizedTest
  @MethodSource("getConstructorTestArgProvider")
  void getConstructor(
      JavaClassSource classSource,
      org.jboss.forge.roaster.model.source.MethodSource<JavaClassSource> expectedConstructor) {
    if (Objects.isNull(expectedConstructor)) {
      assertTrue(JavaClassSourceUtils.getConstructor(classSource).isEmpty());
    } else {
      assertEquals(expectedConstructor, JavaClassSourceUtils.getConstructor(classSource).get());
    }
  }

  @ParameterizedTest
  @MethodSource("copyPropsTestArgProvider")
  void copyPropsTest(JavaClassSource from, JavaClassSource target, JavaClassSource expected) {
    JavaClassSourceUtils.copyProps(from, target);
    if (Objects.isNull(from)) {
      assertEquals(target, expected);
    } else if (Objects.nonNull(from.getProperties()) && Objects.nonNull(target.getProperties())) {
      assertIterableEquals(
          expected.getProperties().stream().map(Named::getName).toList(),
          target.getProperties().stream().map(Named::getName).toList());
    }
  }
}
