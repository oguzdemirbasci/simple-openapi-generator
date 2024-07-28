package org.oguzdem.json.bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

/**
 * Field generator for the double field. The field is a double object. The field is added to the
 * constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public final class DoubleFieldGenerator extends NumberFieldGenerator {
  /**
   * Constructor for the DoubleFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public DoubleFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {
    super(rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    if (defaultObject instanceof Number) {
      return defaultObject + "D";
    }
    return null;
  }

  @Override
  protected Class<?> getTypeClass() {
    return Double.class;
  }
}
