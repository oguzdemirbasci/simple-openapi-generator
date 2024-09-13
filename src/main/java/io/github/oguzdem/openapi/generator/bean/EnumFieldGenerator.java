package io.github.oguzdem.openapi.generator.bean;

import io.github.oguzdem.openapi.generator.TypeGenerator;
import io.github.oguzdem.openapi.generator.utils.NameUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 * Field generator for the enum field. The field is an enum const. The field is added to the
 * constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public final class EnumFieldGenerator extends FieldGenerator {
  /**
   * Constructor for the EnumFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public EnumFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {
    super(rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
  }

  /**
   * Generate the field for the enum. The field is an enum const. The field is added to the
   * constructor if it is not already added.
   *
   * @return {@link PropertySource} object of the generated field
   */
  @Override
  public PropertySource<JavaClassSource> generateField() {
    String objectType =
        TypeGenerator.getOrGenerateType(
            NameUtils.toJavaTypeNameFormat(name + "_Enum"), schema, components);
    PropertySource<JavaClassSource> propertySource =
        javaClassSource.addProperty(objectType, NameUtils.toJavaFieldNameFormat(name));
    addStandardAnnotations(propertySource);
    addConstructorParameter();
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    if (defaultObject instanceof String) {
      return fieldSource.getType().getName()
          + "."
          + NameUtils.toEnumValueFormat(String.valueOf(defaultObject));
    }
    return null;
  }
}
