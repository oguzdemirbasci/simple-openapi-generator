package org.oguzdem.openapi.generator.bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.ObjectUtils;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.oguzdem.openapi.generator.TypeGenerator;
import org.oguzdem.openapi.generator.utils.JavaClassSourceUtils;
import org.oguzdem.openapi.generator.utils.NameUtils;

/**
 * Field generator for the object field. The field is an object. The field is added to the
 * constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public final class ObjectFieldGenerator extends FieldGenerator {
  /**
   * Constructor for the ObjectFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public ObjectFieldGenerator(
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
    String objectType;
    if (JavaClassSourceUtils.isJavaObject(schema)) {
      objectType = "Object";
    } else if (ObjectUtils.isEmpty(schema.get$ref())) {
      objectType = generateInnerType(name, schema);
    } else {
      objectType =
          TypeGenerator.getOrGenerateType(
              NameUtils.toJavaTypeNameFormat(javaClassSource.getName() + "_" + name),
              schema,
              components);
    }
    PropertySource<JavaClassSource> propertySource =
        javaClassSource.addProperty(objectType, NameUtils.toJavaFieldNameFormat(name));
    addStandardAnnotations(propertySource);
    addConstructorParameter();
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    // Todo need to test what is loaded here
    return null;
  }
}
