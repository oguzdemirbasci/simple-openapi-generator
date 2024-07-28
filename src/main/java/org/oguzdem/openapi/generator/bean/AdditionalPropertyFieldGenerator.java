package org.oguzdem.json.bean;

import static org.oguzdem.json.PojoGenerator.BEAN_VALIDATION_ANNOTATIONS_ENABLED;
import static org.oguzdem.json.utils.JavaClassSourceUtils.isJavaObject;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Singular;
import org.apache.commons.lang3.ObjectUtils;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.oguzdem.json.TypeGenerator;
import org.oguzdem.json.utils.NameUtils;

/**
 * Field generator for the additional properties field. The additional properties field is a map of
 * string to object. The field is annotated with @{@link Singular}, @{@link JsonAnyGetter}
 * and @{@link JsonAnySetter}. The getter method is annotated with @{@link JsonIgnore}. The field is
 * also annotated with {@code @Size(min = minProperties, max = maxProperties)} if minProperties or
 * maxProperties are not null. The field is added to the constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public final class AdditionalPropertyFieldGenerator extends FieldGenerator {
  public static final String ADDITIONAL_PROPERTIES_FIELD_NAME = "additionalProperties";
  public static final String ADDITIONAL_PROPERTIES_GETTER_NAME = "getAdditionalProperties";

  /**
   * Constructor for the AdditionalPropertyFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param schema The schema object
   * @param components The components object as look up for referenced schemas
   */
  public AdditionalPropertyFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      Schema<?> schema,
      Components components) {
    super(
        rootJavaClassSource,
        javaClassSource,
        ADDITIONAL_PROPERTIES_FIELD_NAME,
        schema,
        false,
        components);
  }

  /**
   * Generate the field for the additional properties.
   * <li>The field is a map of string to object.
   * <li>The field is annotated with @{@link Singular}, @{@link JsonAnyGetter} and @{@link
   *     JsonAnySetter}.
   * <li>The getter method is annotated with @{@link JsonIgnore}.
   * <li>The field is also annotated with {@code @Size(min = minProperties, max = maxProperties)} if
   *     minProperties or maxProperties are not null.
   * <li>The field is added to the constructor if it is not already added.
   *
   * @return {@link PropertySource} object of the generated field
   */
  public PropertySource<JavaClassSource> generateField() {
    super.generateField();
    javaClassSource.addImport(ImmutableMap.class);
    rootJavaClassSource.addImport(ImmutableMap.class);
    PropertySource<JavaClassSource> propertySource =
        javaClassSource.getProperty(ADDITIONAL_PROPERTIES_FIELD_NAME);
    Object additionalProps = schema.getAdditionalProperties();

    String valueType = "Object";

    if (additionalProps instanceof Schema && !(isJavaObject((Schema<?>) additionalProps))) {
      valueType =
          TypeGenerator.getOrGenerateType(
              NameUtils.toJavaTypeNameFormat(javaClassSource.getName() + "_additionalProperties"),
              (Schema<?>) additionalProps,
              components,
              javaClassSource,
              rootJavaClassSource);
    }

    propertySource.setType("ImmutableMap<String, %s>".formatted(valueType));
    addConstructorParameter();

    FieldSource<JavaClassSource> fieldSource = propertySource.getField();
    fieldSource.addAnnotation(Singular.class);
    fieldSource.addAnnotation(JsonAnyGetter.class);
    fieldSource.addAnnotation(JsonAnySetter.class);
    rootJavaClassSource.addImport(Singular.class);
    rootJavaClassSource.addImport(JsonAnyGetter.class);
    rootJavaClassSource.addImport(JsonAnySetter.class);

    MethodSource<JavaClassSource> getter = javaClassSource.addMethod();
    getter.setName(ADDITIONAL_PROPERTIES_GETTER_NAME);
    getter.setReturnType(fieldSource.getType());
    getter.setBody("return this.%s;".formatted(ADDITIONAL_PROPERTIES_FIELD_NAME));
    getter.addAnnotation(JsonIgnore.class);
    rootJavaClassSource.addImport(JsonIgnore.class);

    if (BEAN_VALIDATION_ANNOTATIONS_ENABLED) {
      if (ObjectUtils.isNotEmpty(this.schema.getMinProperties())) {
        if (!fieldSource.hasAnnotation(Size.class)) {
          fieldSource.addAnnotation(Size.class);
          rootJavaClassSource.addImport(Size.class);
        }
        fieldSource
            .getAnnotation(Size.class)
            .setLiteralValue("min", this.schema.getMinProperties().toString());
      }
      if (ObjectUtils.isNotEmpty(this.schema.getMaxProperties())) {
        if (!fieldSource.hasAnnotation(Size.class)) {
          fieldSource.addAnnotation(Size.class);
          rootJavaClassSource.addImport(Size.class);
        }
        fieldSource
            .getAnnotation(Size.class)
            .setLiteralValue("max", this.schema.getMaxProperties().toString());
      }
    }

    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    // No default for additional properties.
    return "";
  }
}
