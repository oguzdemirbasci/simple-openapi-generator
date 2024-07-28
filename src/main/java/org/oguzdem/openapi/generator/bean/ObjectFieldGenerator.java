package org.oguzdem.json.bean;

import static org.oguzdem.json.utils.JavaClassSourceUtils.isJavaObject;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.ObjectUtils;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.oguzdem.json.TypeGenerator;
import org.oguzdem.json.utils.NameUtils;

/**
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
    if (isJavaObject(schema)) {
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
