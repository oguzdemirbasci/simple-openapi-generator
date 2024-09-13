package com.github.oguzdem.openapi.generator.bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import com.github.oguzdem.openapi.generator.Config;
import com.github.oguzdem.openapi.generator.constraints.MultipleOf;

/**
 * Abstract class for the number field generator. The field is a number object. The field is added
 * to the constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public abstract class NumberFieldGenerator extends FieldGenerator {
  /**
   * Base constructor for the NumberFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public NumberFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {
    super(rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
  }

  /**
   * Generates the field for the number field. Adds the field to the constructor if it is not
   * already added.
   *
   * @return {@link PropertySource} object for the field
   */
  public PropertySource<JavaClassSource> generateField() {
    super.generateField();
    PropertySource<JavaClassSource> propertySource = javaClassSource.getProperty(this.name);
    FieldSource<JavaClassSource> fieldSource = propertySource.getField();

    if (Config.isBeanValidationEnabled()) {
      if (ObjectUtils.isNotEmpty(this.schema.getMinimum())) {
        AnnotationSource<JavaClassSource> minAnnotation = fieldSource.addAnnotation(Min.class);
        rootJavaClassSource.addImport(Min.class);
        if (Objects.equals(this.schema.getExclusiveMinimum(), Boolean.TRUE)) {
          minAnnotation.setLiteralValue(
              "value", String.valueOf(this.schema.getMinimum().longValue() + 1));
        } else {
          minAnnotation.setLiteralValue(
              "value", String.valueOf(this.schema.getMinimum().longValue()));
        }
      }
      if (ObjectUtils.isNotEmpty(this.schema.getMaximum())) {
        AnnotationSource<JavaClassSource> maxAnnotation = fieldSource.addAnnotation(Max.class);
        rootJavaClassSource.addImport(Max.class);
        if (Objects.equals(this.schema.getExclusiveMinimum(), Boolean.TRUE)) {
          maxAnnotation.setLiteralValue(
              "value", String.valueOf(this.schema.getMaximum().longValue() - 1));
        } else {
          maxAnnotation.setLiteralValue(
              "value", String.valueOf(this.schema.getMaximum().longValue()));
        }
      }
      if (ObjectUtils.isNotEmpty(this.schema.getMultipleOf())) {
        AnnotationSource<JavaClassSource> multipleOfAnnotation =
            fieldSource.addAnnotation(MultipleOf.class);
        rootJavaClassSource.addImport(MultipleOf.class);
        multipleOfAnnotation.setLiteralValue("value", this.schema.getMultipleOf().toString());
      }
    }

    addConstructorParameter();
    return propertySource;
  }
}
