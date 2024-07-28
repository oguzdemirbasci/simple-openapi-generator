package org.oguzdem.openapi.generator.bean;

import static io.swagger.v3.parser.util.SchemaTypeUtil.EMAIL_FORMAT;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.oguzdem.openapi.generator.Config;

/**
 * Field generator for the string field. The field is a string object. The field is added to the
 * constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public final class StringFieldGenerator extends FieldGenerator {
  /**
   * Constructor for the StringFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public StringFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {
    super(rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
  }

  /**
   * Generates the field for the string field. Adds the field to the constructor if it is not
   * already added. For the string field, the following validations are added:
   * <li>Pattern: If the schema has a pattern, the pattern annotation is added to the field.
   * <li>Size: If the schema has a minimum length, the size annotation is added to the field with
   *     the minimum length. If the schema has a maximum length, the size annotation is added to the
   *     field with the maximum length.
   * <li>Email: If the schema has a format and the format is email, the email annotation is added to
   *     the field.
   *
   * @return {@link PropertySource} object for the field
   */
  @Override
  public PropertySource<JavaClassSource> generateField() {
    super.generateField();
    PropertySource<JavaClassSource> propertySource = javaClassSource.getProperty(this.name);
    FieldSource<JavaClassSource> fieldSource = propertySource.getField();

    if (Config.isBeanValidationEnabled()) {
      if (StringUtils.isNotBlank(this.schema.getPattern())) {
        if (!fieldSource.hasAnnotation(Pattern.class)) {
          fieldSource.addAnnotation(Pattern.class);
          rootJavaClassSource.addImport(Pattern.class);
        }
        fieldSource.getAnnotation(Pattern.class).setStringValue("regexp", this.schema.getPattern());
      }
      if (ObjectUtils.isNotEmpty(this.schema.getMinLength())) {
        if (!fieldSource.hasAnnotation(Size.class)) {
          fieldSource.addAnnotation(Size.class);
          rootJavaClassSource.addImport(Size.class);
        }
        fieldSource
            .getAnnotation(Size.class)
            .setLiteralValue("min", this.schema.getMinLength().toString());
      }
      if (ObjectUtils.isNotEmpty(this.schema.getMaxLength())) {
        if (!fieldSource.hasAnnotation(Size.class)) {
          fieldSource.addAnnotation(Size.class);
          rootJavaClassSource.addImport(Size.class);
        }
        fieldSource
            .getAnnotation(Size.class)
            .setLiteralValue("max", this.schema.getMaxLength().toString());
      }

      if (ObjectUtils.isNotEmpty(this.schema.getFormat())
          && this.schema.getFormat().equals(EMAIL_FORMAT)) {
        if (!fieldSource.hasAnnotation(Email.class)) {
          fieldSource.addAnnotation(Email.class);
          rootJavaClassSource.addImport(Email.class);
        }
      }

      if (ObjectUtils.isNotEmpty(this.schema.getFormat())
          && this.schema.getFormat().equals(EMAIL_FORMAT)) {
        if (!fieldSource.hasAnnotation(Email.class)) {
          fieldSource.addAnnotation(Email.class);
          rootJavaClassSource.addImport(Email.class);
        }
      }
    }

    addConstructorParameter();
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    if (defaultObject instanceof String) {
      return "\"%s\"".formatted(defaultObject);
    }
    return null;
  }
}
