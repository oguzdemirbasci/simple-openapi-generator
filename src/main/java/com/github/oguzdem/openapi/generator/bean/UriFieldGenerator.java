package com.github.oguzdem.openapi.generator.bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.net.URI;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 * Field generator for the URI field. The field is a URI object. The field is added to the
 * constructor
 *
 * @author Oguz Demirbasci
 */
public final class UriFieldGenerator extends FieldGenerator {
  private static final String LITERAL_INITIALIZER_TEMPLATE = "URI.create(\"%s\")";

  /**
   * Constructor for the UriFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public UriFieldGenerator(
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
    PropertySource<JavaClassSource> propertySource = super.generateField();
    rootJavaClassSource.addImport(URI.class);
    addConstructorParameter();
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    if (defaultObject instanceof String) {
      return String.format(LITERAL_INITIALIZER_TEMPLATE, defaultObject);
    }
    return null;
  }

  @Override
  protected Class<?> getTypeClass() {
    return URI.class;
  }
}
