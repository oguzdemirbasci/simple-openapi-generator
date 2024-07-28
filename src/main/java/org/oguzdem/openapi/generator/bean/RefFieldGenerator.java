package org.oguzdem.openapi.generator.bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import lombok.NonNull;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.oguzdem.openapi.generator.TypeGenerator;
import org.oguzdem.openapi.generator.utils.NameUtils;

/**
 * Field generator for the ref field. The field is a reference to another schema. The field is added
 * to the constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public class RefFieldGenerator extends FieldGenerator {
  /**
   * Constructor for the RefFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public RefFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {
    super(rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
  }

  @Override
  public PropertySource<JavaClassSource> generateField() {
    String objectType =
        TypeGenerator.getOrGenerateType(
            name, schema, components, javaClassSource, rootJavaClassSource);
    PropertySource<JavaClassSource> propertySource =
        javaClassSource.addProperty(objectType, NameUtils.toJavaFieldNameFormat(name));

    addStandardAnnotations(propertySource);
    FieldSource<JavaClassSource> fieldSource = propertySource.getField();

    if (isRequired) {
      fieldSource.addAnnotation(NonNull.class);
    }

    addConstructorParameter();
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    return "";
  }
}
