package org.oguzdem.openapi.generator.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 * Field generator for the date field. The field is a date object. The field is annotated
 * with @{@link JsonFormat}. The field is added to the constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public final class DateFieldGenerator extends FieldGenerator {
  /**
   * Constructor for the DateFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public DateFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {
    super(rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
  }

  /**
   * Generate the field for the date. The field is a date object. The field is annotated
   * with @{@link JsonFormat}. The field is added to the constructor if it is not already added.
   *
   * @return {@link PropertySource} object of the generated field
   */
  @Override
  public PropertySource<JavaClassSource> generateField() {
    super.generateField();
    PropertySource<JavaClassSource> propertySource = javaClassSource.getProperty(this.name);
    FieldSource<JavaClassSource> fieldSource = propertySource.getField();

    rootJavaClassSource.addImport(JsonFormat.class);

    fieldSource
        .addAnnotation(JsonFormat.class)
        .setEnumValue("shape", JsonFormat.Shape.STRING)
        .setStringValue("pattern", "yyyy-MM-dd");

    addConstructorParameter();
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    if (defaultObject instanceof Date) {
      getOrCreateConstructor().addThrows(ParseException.class);
      rootJavaClassSource.addImport(SimpleDateFormat.class);
      javaClassSource
          .addProperty(SimpleDateFormat.class, "FORMAT")
          .removeAccessor()
          .removeMutator()
          .getField()
          .setPrivate()
          .setStatic(true)
          .setFinal(true)
          .setLiteralInitializer("new SimpleDateFormat(\"yyyy-MM-dd\")");
      String time = ((Date) defaultObject).toInstant().toString();
      return "FORMAT.parse(\"%s\")".formatted(time.split("T")[0]);
    }
    return null;
  }

  @Override
  protected Class<?> getTypeClass() {
    return Date.class;
  }
}
