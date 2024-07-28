package org.oguzdem.json.bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.net.MalformedURLException;
import java.net.URL;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 * Field generator for the URL field. The field is a URL object. The field is added to the
 * constructor if it is not already added.
 *
 * @author Oguz Demirbasci
 */
public final class UrlFieldGenerator extends FieldGenerator {
  private static final String LITERAL_INITIALIZER_TEMPLATE = "new URL(\"%s\")";

  /**
   * Constructor for the UrlFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public UrlFieldGenerator(
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
    addConstructorParameter();
    rootJavaClassSource.addImport(URL.class);
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    if (defaultObject instanceof String) {
      MethodSource<JavaClassSource> constructor = getOrCreateConstructor();
      constructor.addThrows(MalformedURLException.class);
      rootJavaClassSource.addImport(MalformedURLException.class);
      return String.format(LITERAL_INITIALIZER_TEMPLATE, defaultObject);
    }
    return null;
  }

  @Override
  protected Class<?> getTypeClass() {
    return URL.class;
  }
}
