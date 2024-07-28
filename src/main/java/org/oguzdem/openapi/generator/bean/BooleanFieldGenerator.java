package org.oguzdem.json.bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 * Field generator for the boolean field. The field is a boolean object. The field is added to the
 * constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public final class BooleanFieldGenerator extends FieldGenerator {
  /**
   * Constructor for the BooleanFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public BooleanFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {
    super(rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
  }

  /**
   * Generate the field for the boolean. The field is a boolean object. The field is added to the
   * constructor if it is not already added.
   *
   * @return {@link PropertySource} object of the generated field
   */
  @Override
  public PropertySource<JavaClassSource> generateField() {
    PropertySource<JavaClassSource> propertySource = super.generateField();
    addConstructorParameter();
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    if (defaultObject instanceof Boolean) {
      return String.valueOf(((Boolean) defaultObject).booleanValue());
    }
    return null;
  }

  @Override
  protected Class<?> getTypeClass() {
    return Boolean.class;
  }
}
