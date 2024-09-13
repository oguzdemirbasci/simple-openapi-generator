package io.github.oguzdem.openapi.generator.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.time.OffsetDateTime;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 * Field generator for the date time field. The field is a date time object. The field is annotated
 * with @{@link JsonFormat}. The field is added to the constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public final class DateTimeFieldGenerator extends FieldGenerator {
  /**
   * Constructor for the DateTimeFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public DateTimeFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {
    super(rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
  }

  /**
   * Generate the field for the date time. The field is a date time object. The field is annotated
   * with @{@link JsonFormat}. The field is added to the constructor if it is not already added.
   *
   * @return {@link PropertySource} object of the generated field
   */
  @Override
  public PropertySource<JavaClassSource> generateField() {
    super.generateField();
    PropertySource<JavaClassSource> propertySource = javaClassSource.getProperty(this.name);
    FieldSource<JavaClassSource> fieldSource = propertySource.getField();

    fieldSource
        .addAnnotation(JsonFormat.class)
        .setEnumValue("shape", JsonFormat.Shape.STRING)
        .setStringValue("pattern", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    addConstructorParameter();
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    if (defaultObject instanceof OffsetDateTime) {
      this.javaClassSource.addImport(OffsetDateTime.class);
      return "OffsetDateTime.parse(\"%s\")".formatted(defaultObject.toString());
    }
    return null;
  }

  @Override
  protected Class<?> getTypeClass() {
    return OffsetDateTime.class;
  }
}
