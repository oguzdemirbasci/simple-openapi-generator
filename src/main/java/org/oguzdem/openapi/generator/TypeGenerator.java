package org.oguzdem.openapi.generator;

import static org.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isAllOf;
import static org.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isAnyOf;
import static org.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isArray;
import static org.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isEnum;
import static org.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isJavaObject;
import static org.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isObject;
import static org.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isOneOf;
import static org.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isRef;

import com.google.common.collect.ImmutableList;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.oguzdem.openapi.generator.utils.NameUtils;

/**
 * Type generator class that generates Java types from JSON schemas.
 *
 * @author Oguz Demirbasci
 */
@Slf4j
public class TypeGenerator {

  private static final Map<String, Schema<?>> GENERATED_NAME_TO_SCHEMA = new HashMap<>();
  private static final Map<Schema<?>, String> SCHEMA_TO_GENERATED_NAME = new HashMap<>();
  private static final Map<String, Integer> GENERATED_OBJECTS_OCCURRENCE = new HashMap<>();

  /**
   * Generates a Java type from the given schema using {@link PojoGenerator} if the schema is an
   * object or an enum. If the schema is a reference, it will try to find the referenced schema in
   * the components object and generate the type from the referenced schema. If the schema is an
   * array, it will generate a list type. If the schema is a Java object, it will return "Object".
   *
   * @param name the name of the type
   * @param schema the schema to generate the type from
   * @param components the components object that contains the schemas
   * @param javaClassSources the Java class sources to add imports
   * @return the generated type name as a string
   */
  public static String getOrGenerateType(
      String name, Schema<?> schema, Components components, JavaClassSource... javaClassSources) {
    if (StringUtils.isBlank(name)) {
      log.error("no type is generated for empty name");
    }

    if (isRef(schema)) {
      if (ObjectUtils.isNotEmpty(components) && ObjectUtils.isNotEmpty(components.getSchemas())) {

        String refName =
            StringUtils.removeStart(schema.get$ref(), Components.COMPONENTS_SCHEMAS_REF);
        if (Objects.nonNull(components.getSchemas().get(refName))) {
          Schema<?> referencedSchema = components.getSchemas().get(refName);
          return getOrGenerateType(refName, referencedSchema, components, javaClassSources);
        }
      }
      throw new IllegalArgumentException(
          "Referenced schema is not found in the schema map! %s".formatted(schema.get$ref()));
    }

    if (isJavaObject(schema)) {
      return "Object";
    }

    if (isArray(schema)) {
      for (JavaClassSource javaClassSource : javaClassSources) {
        javaClassSource.addImport(ImmutableList.class);
      }

      if (Objects.isNull(schema.getItems())) {
        return "ImmutableList<Object>";
      }
      return "ImmutableList<" + getOrGenerateType(name, schema.getItems(), components) + ">";
    }

    if (isObject(schema)
        || isEnum(schema)
        || isAnyOf(schema)
        || isAllOf(schema)
        || isOneOf(schema)) {
      return getOrGenerateClassType(name, schema, components);
    }
    return NameUtils.toJavaTypeNameFormat(schema.getType());
  }

  private static String getOrGenerateClassType(
      String name, Schema<?> schema, Components components) {
    if (GENERATED_NAME_TO_SCHEMA.containsKey(name)
        && GENERATED_NAME_TO_SCHEMA.get(name).equals(schema)) {
      return NameUtils.toJavaTypeNameFormat(name);
    }
    if (SCHEMA_TO_GENERATED_NAME.containsKey(schema)) {
      log.info("Schema is already generated: %s".formatted(SCHEMA_TO_GENERATED_NAME.get(schema)));
      return NameUtils.toJavaTypeNameFormat(SCHEMA_TO_GENERATED_NAME.get(schema));
    }

    String objectName = getObjectName(name);
    PojoGenerator.generate(objectName, schema, components);
    GENERATED_NAME_TO_SCHEMA.put(objectName, schema);
    SCHEMA_TO_GENERATED_NAME.put(schema, objectName);
    GENERATED_OBJECTS_OCCURRENCE.putIfAbsent(name, 0);
    GENERATED_OBJECTS_OCCURRENCE.computeIfPresent(name, (k, v) -> v + 1);
    return NameUtils.toJavaTypeNameFormat(objectName);
  }

  private static String getObjectName(String name) {
    if (GENERATED_OBJECTS_OCCURRENCE.containsKey(name)) {
      int noNameCounter = GENERATED_OBJECTS_OCCURRENCE.get(name);
      noNameCounter += 1;
      name += noNameCounter;
    }
    return name;
  }
}
