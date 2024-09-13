package io.github.oguzdem.openapi.generator.bean;

import static io.github.oguzdem.openapi.generator.utils.JavaClassSourceUtils.fillEnumSourceBySchema;
import static io.github.oguzdem.openapi.generator.utils.JavaClassSourceUtils.fillJavaClassSourceBySchema;
import static io.github.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isEnum;
import static io.github.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import io.github.oguzdem.openapi.generator.utils.NameUtils;
import io.swagger.util.PrimitiveType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 * Base class for field generators. Field generators are used to generate fields for the given
 * schema and add them to the java class source object. Also, they add the fields to the constructor
 * if they are not already added. If the field is required, they add the NonNull annotation.
 *
 * @author Oguz Demirbasci
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class FieldGenerator {
  private static final String CONSTRUCTOR_FIELD_SET_TEMPLATE = "this.%s = %s;";
  private static final boolean ACCESSIBLE = false;
  private static final boolean MUTABLE = false;
  protected JavaClassSource rootJavaClassSource;
  protected JavaClassSource javaClassSource;
  protected String name;
  protected Schema<?> schema;
  protected Boolean isRequired;
  protected Components components;

  /**
   * Generate field for the given schema and add it to the java class source object. Also add the
   * field to the constructor if it is not already added. If the field is required, add the NonNull
   * annotation.
   *
   * @return {@link PropertySource} object of the generated field
   */
  public PropertySource<JavaClassSource> generateField() {

    Class<?> typeClass = getPrimitiveTypeIfNotNullable(getTypeClass());
    PropertySource<JavaClassSource> propertySource = javaClassSource.addProperty(typeClass, name);
    addStandardAnnotations(propertySource);
    FieldSource<JavaClassSource> fieldSource = propertySource.getField();

    if (isRequired) {
      fieldSource.addAnnotation(NonNull.class);
      rootJavaClassSource.addImport(NonNull.class);
    }
    return propertySource;
  }

  /**
   * Add constructor parameter for the field if it is not already added. If the field has a default
   * value, add the default initializer.
   */
  protected void addConstructorParameter() {
    if (ObjectUtils.isEmpty(javaClassSource.getProperty(this.name))) {
      return;
    }

    MethodSource<JavaClassSource> constructor = getOrCreateConstructor();
    Type<JavaClassSource> type = javaClassSource.getProperty(this.name).getType();
    constructor.addParameter(getTypeString(type), this.name);

    // If default is set, initializer will be created by addDefaultInitializer()
    if (Objects.isNull(schema.getDefault())) {
      constructor.setBody(
          Strings.nullToEmpty(constructor.getBody())
              + String.format(CONSTRUCTOR_FIELD_SET_TEMPLATE, this.name, this.name));
    } else {
      String fieldInitiator =
          String.format(
              CONSTRUCTOR_FIELD_SET_TEMPLATE,
              this.name,
              "Objects.isNull(%s) ? %s : %s"
                  .formatted(
                      this.name,
                      getDefaultInitializerValue(
                          schema.getDefault(), javaClassSource.getProperty(this.name).getField()),
                      this.name));
      constructor.setBody(Strings.nullToEmpty(constructor.getBody()) + fieldInitiator);
      rootJavaClassSource.addImport(Objects.class);
    }
  }

  /**
   * Get the default initializer value for the field to be used in the constructor.
   *
   * @param defaultObject Default value of the field
   * @param fieldSource Field source of the field
   * @return Default initializer value
   */
  abstract String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource);

  /**
   * Get the string representation of the type. Any type arguments will be included in the string.
   *
   * @param type Type object
   * @return String representation of the type
   */
  private String getTypeString(Type<?> type) {
    StringBuilder builder = new StringBuilder(type.getName());
    if (ObjectUtils.isNotEmpty(type.getTypeArguments())) {
      builder
          .append("<")
          .append(
              String.join(",", type.getTypeArguments().stream().map(this::getTypeString).toList()))
          .append(">");
    }
    return builder.toString();
  }

  /**
   * Add standard annotations to the field. These include Jackson annotations, accessibility and
   * mutability.
   *
   * @param propertySource Property source of the field
   */
  protected void addStandardAnnotations(PropertySource<JavaClassSource> propertySource) {
    propertySource.setAccessible(ACCESSIBLE);
    propertySource.setMutable(MUTABLE);

    FieldSource<JavaClassSource> fieldSource = propertySource.getField();
    fieldSource.setVisibility(Visibility.PACKAGE_PRIVATE);
    fieldSource.setFinal(false);
    fieldSource.addAnnotation(JsonProperty.class).setStringValue(name);
    rootJavaClassSource.addImport(JsonProperty.class);
  }

  /**
   * Get the primitive type if the schema is not nullable. Wrapper classes are used by default.
   *
   * @param classType Class type to be checked
   * @return Primitive type if the schema is not nullable, otherwise the class type
   */
  protected Class<?> getPrimitiveTypeIfNotNullable(Class<?> classType) {
    // Generate Primitive Type If Not Nullable (Wrapper Classes are used by default)
    if (Objects.equals(this.schema.getNullable(), Boolean.FALSE)) {
      Class<?> primitive = ClassUtils.wrapperToPrimitive(classType);
      if (ObjectUtils.isNotEmpty(primitive)) {
        return primitive;
      }
    }
    return classType;
  }

  /**
   * Get the class of the type of the field.
   *
   * @return Class of the type of the field
   */
  protected Class<?> getTypeClass() {
    PrimitiveType type = PrimitiveType.fromName(schema.getType());
    return type.getKeyClass();
  }

  /**
   * Get or create the constructor of the class. If the constructor is already created, return the
   * existing one. Otherwise, create a new constructor and return it.
   *
   * @return Constructor of the class
   */
  protected MethodSource<JavaClassSource> getOrCreateConstructor() {
    Optional<MethodSource<JavaClassSource>> constructorOpt =
        this.javaClassSource.getMethods().stream()
            .filter(method -> method.getName().equals(javaClassSource.getName()))
            .findFirst();
    if (constructorOpt.isEmpty()) {
      MethodSource<JavaClassSource> constructor = javaClassSource.addMethod();
      constructor.setConstructor(true).setPublic();
      constructor.addAnnotation(Builder.class);
      return constructor;
    } else {
      return constructorOpt.get();
    }
  }

  /**
   * Generate inner type for the given schema and add it to the java class source object.
   *
   * @param propName Name of the property
   * @param schema Schema of the property
   * @return Name of the inner type
   */
  protected String generateInnerType(String propName, Schema<?> schema) {
    if (StringUtils.isEmpty(propName)) {
      return StringUtils.EMPTY;
    }
    String objectType = NameUtils.toJavaTypeNameFormat(propName);
    if (isEnum(schema)) {
      JavaEnumSource innerEnum = Roaster.create(JavaEnumSource.class);
      innerEnum.setName(NameUtils.toJavaTypeNameFormat(name));
      fillEnumSourceBySchema(innerEnum, schema);
    } else if (isObject(schema)) {
      JavaClassSource innerClass = Roaster.create(JavaClassSource.class);
      innerClass.setName(NameUtils.toJavaTypeNameFormat(name));
      innerClass.setStatic(true);
      fillJavaClassSourceBySchema(rootJavaClassSource, innerClass, schema, components);
      if (javaClassSource.equals(rootJavaClassSource)) {
        javaClassSource.addNestedType(innerClass);
      } else {
        rootJavaClassSource.addNestedType(innerClass);
      }
    }
    return objectType;
  }
}
