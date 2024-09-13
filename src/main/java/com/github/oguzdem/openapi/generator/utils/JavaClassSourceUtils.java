package com.github.oguzdem.openapi.generator.utils;

import static io.swagger.v3.parser.util.SchemaTypeUtil.OBJECT_TYPE;
import static io.swagger.v3.parser.util.SchemaTypeUtil.STRING_TYPE;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.InterfaceCapableSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import com.github.oguzdem.openapi.generator.PojoGenerator;
import com.github.oguzdem.openapi.generator.TypeGenerator;
import com.github.oguzdem.openapi.generator.bean.FieldGeneratorFactory;

/**
 * Utility class for java class source operations.
 *
 * @author Oguz Demirbasci
 */
@Slf4j
public class JavaClassSourceUtils {

  /**
   * Checks if the schema is a reference.
   *
   * @param schema schema
   * @return true if the schema is a reference
   */
  public static boolean isRef(Schema<?> schema) {
    return Objects.nonNull(schema) && StringUtils.isNotBlank(schema.get$ref());
  }

  /**
   * Checks if the schema is a java {@link Object} type.
   *
   * @param schema schema
   * @return true if the schema is a java {@link Object} type
   */
  public static boolean isJavaObject(Schema<?> schema) {
    return Objects.nonNull(schema)
        && OBJECT_TYPE.equals(schema.getType())
        && ObjectUtils.isEmpty(schema.getProperties())
        && ObjectUtils.isEmpty(schema.getEnum())
        && ObjectUtils.isEmpty(schema.getAdditionalProperties())
        && ObjectUtils.isEmpty(schema.getAnyOf())
        && ObjectUtils.isEmpty(schema.getAllOf())
        && ObjectUtils.isEmpty(schema.getOneOf())
        && ObjectUtils.isEmpty(schema.get$ref());
  }

  /**
   * Checks if the schema is an openapi object type.
   *
   * @param schema schema
   * @return true if the schema is an openapi object type
   */
  public static boolean isObject(Schema<?> schema) {
    return Objects.nonNull(schema) && OBJECT_TYPE.equals(schema.getType());
  }

  /**
   * Checks if the schema is an openapi enum type.
   *
   * @param schema schema
   * @return true if the schema is an openapi enum type
   */
  public static boolean isEnum(Schema<?> schema) {
    return Objects.nonNull(schema)
        && ObjectUtils.isNotEmpty(schema.getEnum())
        && STRING_TYPE.equals(schema.getType());
  }

  /**
   * Checks if the schema is an openapi array type.
   *
   * @param schema schema
   * @return true if the schema is an openapi array type
   */
  public static boolean isArray(Schema<?> schema) {
    return Objects.nonNull(schema) && "array".equals(schema.getType());
  }

  /**
   * Checks if the schema is an openapi allOf type.
   *
   * @param schema schema
   * @return true if the schema is an openapi allOf type
   */
  public static boolean isAllOf(Schema<?> schema) {
    return Objects.nonNull(schema) && ObjectUtils.isNotEmpty(schema.getAllOf());
  }

  /**
   * Checks if the schema is an openapi oneOf type.
   *
   * @param schema schema
   * @return true if the schema is an openapi oneOf type
   */
  public static boolean isOneOf(Schema<?> schema) {
    return Objects.nonNull(schema) && ObjectUtils.isNotEmpty(schema.getOneOf());
  }

  /**
   * Checks if the schema is an openapi anyOf type.
   *
   * @param schema schema
   * @return true if the schema is an openapi anyOf type
   */
  public static boolean isAnyOf(Schema<?> schema) {
    return Objects.nonNull(schema) && ObjectUtils.isNotEmpty(schema.getAnyOf());
  }

  /**
   * Fills the properties of the java class source with the schema.
   *
   * @param javaClass java class source
   * @param schema schema
   * @param components openapi components object
   */
  public static void fillJavaClassSourceBySchema(
      JavaSource<?> javaClass, Schema<?> schema, Components components) {
    fillJavaClassSourceBySchema(javaClass, javaClass, schema, components);
  }

  /**
   * Fills the properties of the java class source with the schema.
   *
   * @param rootClassSource root class source
   * @param javaClass java class source
   * @param schema schema
   * @param components components
   */
  public static void fillJavaClassSourceBySchema(
      @NonNull JavaSource<?> rootClassSource,
      @NonNull JavaSource<?> javaClass,
      @NonNull Schema<?> schema,
      @NonNull Components components) {

    if (!javaClass.hasAnnotation(Value.class)) {
      javaClass.addAnnotation(Value.class);
    }

    if (Objects.nonNull(schema.getProperties())) {
      schema
          .getProperties()
          .forEach(
              (propertyName, propertySchema) -> {
                FieldGeneratorFactory.factory()
                    .schema(propertySchema)
                    .name(NameUtils.toJavaFieldNameFormat(propertyName))
                    .rootClassSource((JavaClassSource) rootClassSource)
                    .javaClassSource((JavaClassSource) javaClass)
                    .isRequired(
                        schema.getRequired() != null && schema.getRequired().contains(propertyName))
                    .components(components)
                    .build()
                    .generateField();
              });
    }

    fillJavaSourceForAllOf(rootClassSource, javaClass, schema, components);
    fillJavaSourceForAnyOf(rootClassSource, javaClass, schema, components);
    fillJavaSourceForOneOf(javaClass, schema, components);

    FieldGeneratorFactory.factory()
        .schema(schema)
        .rootClassSource((JavaClassSource) rootClassSource)
        .javaClassSource((JavaClassSource) javaClass)
        .components(components)
        .buildAdditionalPropertiesGenerator()
        .generateField();
  }

  /**
   * Fills the properties of the java class source with the allOf schemas. If the schema is an allOf
   * schema, it creates a class and adds the allOf schemas as its properties. It also adds the
   * JsonSubTypes annotation to the class.
   *
   * @param rootClassSource root class source
   * @param javaClass java class source
   * @param schema schema
   * @param components components
   */
  private static void fillJavaSourceForAllOf(
      JavaSource<?> rootClassSource,
      JavaSource<?> javaClass,
      Schema<?> schema,
      Components components) {
    if (ObjectUtils.isNotEmpty(schema.getAllOf())) {
      schema
          .getAllOf()
          .forEach(
              allOfSchema -> {
                if (isRef(allOfSchema)) {
                  if (Objects.nonNull(components.getSchemas())) {
                    String refName =
                        StringUtils.removeStart(
                            allOfSchema.get$ref(), Components.COMPONENTS_SCHEMAS_REF);
                    if (Objects.nonNull(components.getSchemas().get(refName))) {
                      Schema<?> referencedSchema = components.getSchemas().get(refName);
                      fillJavaClassSourceBySchema(
                          rootClassSource, javaClass, referencedSchema, components);
                    }
                  }
                } else {
                  fillJavaClassSourceBySchema(rootClassSource, javaClass, allOfSchema, components);
                }
              });
    }
  }

  /**
   * AnyOf behaviour is the same as {@link #fillJavaSourceForAllOf(JavaSource, JavaSource, Schema,
   * Components)} because of the java Pojo and jackson limitations.
   *
   * @param rootClassSource root class source
   * @param javaClass java class source
   * @param schema schema
   * @param components components
   */
  private static void fillJavaSourceForAnyOf(
      JavaSource<?> rootClassSource,
      JavaSource<?> javaClass,
      Schema<?> schema,
      Components components) {

    if (ObjectUtils.isNotEmpty(schema.getAnyOf())) {
      schema
          .getAnyOf()
          .forEach(
              allOfSchema -> {
                if (isRef(allOfSchema)) {
                  if (Objects.nonNull(components.getSchemas())) {
                    String refName =
                        StringUtils.removeStart(
                            allOfSchema.get$ref(), Components.COMPONENTS_SCHEMAS_REF);
                    if (Objects.nonNull(components.getSchemas().get(refName))) {
                      Schema<?> referencedSchema = components.getSchemas().get(refName);
                      fillJavaClassSourceBySchema(
                          rootClassSource, javaClass, referencedSchema, components);
                    }
                  }
                } else {
                  fillJavaClassSourceBySchema(rootClassSource, javaClass, allOfSchema, components);
                }
              });
    }
  }

  /**
   * Fills the java source for oneOf schema. If the schema is a oneOf schema, it creates an
   * interface and adds the oneOf schemas as its subtypes. It also adds the JsonSubTypes annotation
   * to the interface.
   *
   * @param javaClass java class source
   * @param schema schema
   * @param components components
   */
  private static void fillJavaSourceForOneOf(
      JavaSource<?> javaClass, Schema<?> schema, Components components) {

    if (ObjectUtils.isNotEmpty(schema.getOneOf())) {
      JavaSource<?> javasource = Roaster.create(JavaInterfaceSource.class);
      javasource.setName(javaClass.getName());
      String interfaceName =
          TypeGenerator.getOrGenerateType(javaClass.getName(), schema, components);
      List<String> oneOfList = new ArrayList<>();
      schema
          .getOneOf()
          .forEach(
              oneOfSchema -> {
                if (isRef(oneOfSchema)) {
                  if (Objects.nonNull(components.getSchemas())) {
                    String refName =
                        StringUtils.removeStart(
                            oneOfSchema.get$ref(), Components.COMPONENTS_SCHEMAS_REF);
                    if (!isArray(components.getSchemas().get(refName))) {
                      String typeName =
                          TypeGenerator.getOrGenerateType(refName, oneOfSchema, components);
                      oneOfList.add(typeName);
                      InterfaceCapableSource<?> oneOfJavaClass =
                          (InterfaceCapableSource<?>) PojoGenerator.getJavaSource(typeName);
                      if (Objects.nonNull(oneOfJavaClass)) {
                        oneOfJavaClass.addInterface(interfaceName);
                      }
                    }
                  }
                } else if (StringUtils.isNotBlank(oneOfSchema.getTitle())) {
                  String typeName =
                      TypeGenerator.getOrGenerateType(
                          javaClass.getName() + "_" + oneOfSchema.getTitle(),
                          oneOfSchema,
                          components);
                  oneOfList.add(typeName);
                  InterfaceCapableSource<?> oneOfJavaClass =
                      (InterfaceCapableSource<?>) PojoGenerator.getJavaSource(typeName);
                  if (Objects.nonNull(oneOfJavaClass)) {
                    oneOfJavaClass.addInterface(interfaceName);
                  }
                }
              });
      JavaInterfaceSource interfaceSource =
          (JavaInterfaceSource) PojoGenerator.getJavaSource(interfaceName);
      if (!interfaceSource.hasAnnotation(JsonSubTypes.class)) {
        interfaceSource.addAnnotation(JsonSubTypes.class);
      }
      String existing = interfaceSource.getAnnotation(JsonSubTypes.class).getLiteralValue();
      try {
        interfaceSource
            .getAnnotation(JsonSubTypes.class)
            .setLiteralValue(NameUtils.toJsonSubTypesList(oneOfList, existing));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  /**
   * Fills the interface source with the schema. If the schema is a oneOf schema, it creates an
   * interface and adds the oneOf schemas as its subtypes. It also adds the JsonSubTypes annotation
   * to the interface.
   *
   * @param javaInterface java interface source
   * @param schema schema
   * @param components components
   */
  public static void fillInterfaceSourceBySchema(
      JavaInterfaceSource javaInterface, Schema<?> schema, Components components) {
    fillJavaSourceForOneOf(javaInterface, schema, components);
  }

  /**
   * Fills the enum source with the schema. If the schema is an enum schema, it creates the enum and
   * adds the enum values as its constants. It also adds the JsonProperty annotation to the enum.
   *
   * @param javaEnum java enum source
   * @param schema schema
   */
  public static void fillEnumSourceBySchema(@NonNull JavaEnumSource javaEnum, Schema<?> schema) {
    if (ObjectUtils.isEmpty(schema.getEnum())) {
      return;
    }
    schema
        .getEnum()
        .forEach(
            enumValue -> {
              if (ObjectUtils.isNotEmpty(enumValue)) {
                String enumConst = NameUtils.toEnumValueFormat(enumValue.toString());
                EnumConstantSource enumConstantSource = javaEnum.getEnumConstant(enumConst);
                if (Objects.isNull(enumConstantSource)) {
                  enumConstantSource = javaEnum.addEnumConstant(enumConst);
                }
                enumConstantSource
                    .addAnnotation(JsonProperty.class)
                    .setStringValue(enumValue.toString());
                if (Objects.nonNull(schema.getDefault())
                    && enumConstantSource.getName().equals(schema.getDefault())) {
                  enumConstantSource.addAnnotation(JsonEnumDefaultValue.class);
                }
              }
            });
  }

  /**
   * Returns the constructor of the class source. If the constructor is not found, it returns an
   * empty {@link Optional}.
   *
   * @param classSource class source
   * @return constructor of the class source
   */
  public static Optional<MethodSource<JavaClassSource>> getConstructor(
      JavaClassSource classSource) {
    if (Objects.isNull(classSource)) {
      return Optional.empty();
    }
    return classSource.getMethods().stream()
        .filter(method -> method.getName().equals(classSource.getName()))
        .findFirst();
  }

  /**
   * Copies the properties of the source class to the target class.
   *
   * @param source source class
   * @param target target class
   */
  public static void copyProps(JavaClassSource source, JavaClassSource target) {
    if (Objects.isNull(source)
        || ObjectUtils.isEmpty(source.getProperties())
        || Objects.isNull(target)
        || Objects.equals(source, target)) {
      return;
    }
    source
        .getProperties()
        .forEach(
            prop -> {
              PropertySource<JavaClassSource> propertySource =
                  target.addProperty(prop.getType().getName(), prop.getName());
              propertySource.removeAccessor().removeMutator().setAccessible(true);
            });
  }
}
