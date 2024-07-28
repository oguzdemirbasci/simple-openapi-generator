package org.oguzdem.json;

import static org.oguzdem.json.utils.JavaClassSourceUtils.fillEnumSourceBySchema;
import static org.oguzdem.json.utils.JavaClassSourceUtils.fillInterfaceSourceBySchema;
import static org.oguzdem.json.utils.JavaClassSourceUtils.fillJavaClassSourceBySchema;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.oguzdem.json.utils.NameUtils;

/**
 * Pojo generator class that generates Java classes from JSON schemas. It uses Roaster library to
 * generate Java classes. It generates classes with Lombok annotations like @{@link Builder}
 * and @{@link Jacksonized}. It also generates classes with @{@link JsonTypeInfo} annotation for the
 * classes that have <b>oneOf</b> property using the {@link JsonTypeInfo.Id#DEDUCTION} type for Pojo
 * inheritance support.
 *
 * @author Oguz Demirbasci
 */
@Slf4j
public class PojoGenerator {

  public static final boolean BEAN_VALIDATION_ANNOTATIONS_ENABLED = true;
  private static final String DEFAULT_PACKAGE_NAME = "org.oguzdem.json.generated";
  private static final Map<String, JavaSource<?>> JAVA_SOURCE_MAP = new HashMap<>();

  /**
   * Generates a Java class from the given schema. If the schema has an <b>enum</b> property, it
   * will generate an enum class. If the schema has a <b>oneOf</b> property, it will generate an
   * interface class. Otherwise, it will generate a regular class. The generated class will be added
   * to the {@link #JAVA_SOURCE_MAP} map with the given name.
   *
   * @param name the name of the class
   * @param schema the schema to generate the class from
   * @param components the components object that contains the schemas
   * @return the generated class as a string
   */
  public static <T> String generate(String name, @NonNull Schema<T> schema, Components components) {
    String className = "";
    if (StringUtils.isBlank(name)) {
      log.error("no type is generated for empty name");
      className = schema.getTitle();
    }
    if (StringUtils.isNotBlank(name)) {
      className = NameUtils.toJavaTypeNameFormat(name);
    }
    if (Objects.nonNull(schema.getEnum())) {
      return generateEnum(className, schema);
    } else if (Objects.nonNull(schema.getOneOf())) {
      return generateInterface(className, schema, components);
    }
    return generateClass(className, schema, components);
  }

  /**
   * Returns the Java source object with the given name.
   *
   * @param name the name of the Java source object
   * @return the Java source object
   */
  public static JavaSource<?> getJavaSource(String name) {
    return JAVA_SOURCE_MAP.get(name);
  }

  /**
   * Writes the Java sources to the file system. It writes the Java source objects in the {@link
   * #JAVA_SOURCE_MAP} map.
   */
  public static void writeJavaSources() {
    JAVA_SOURCE_MAP.forEach(
        (name, javaSource) -> {
          try (PrintWriter out =
              new PrintWriter("src/main/java/org/oguzdem/json/generated/%s.java".formatted(name))) {
            out.println(javaSource);
          } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  private static <T> String generateClass(
      String name, @NonNull Schema<T> schema, Components components) {
    JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
    addPackageInfo(name, schema, javaClass);
    fillJavaClassSourceBySchema(javaClass, schema, components);
    adjustConstructorByDefaultParams(javaClass, schema);
    JAVA_SOURCE_MAP.put(name, javaClass);
    return javaClass.toString();
  }

  private static void adjustConstructorByDefaultParams(
      JavaClassSource javaClass, Schema<?> schema) {

    boolean removeConstructor;

    if (Objects.isNull(schema.getProperties())) {
      removeConstructor = true;
    } else {
      Optional<Schema> anyDefaultParam =
          schema.getProperties().values().stream()
              .filter(propSchema -> Objects.nonNull(propSchema.getDefault()))
              .findAny();
      removeConstructor = anyDefaultParam.isEmpty();
    }

    // Remove constructor if no param with default value
    if (removeConstructor) {
      Optional<MethodSource<JavaClassSource>> constructorOpt =
          javaClass.getMethods().stream()
              .filter(method -> method.getName().equals(javaClass.getName()))
              .findFirst();
      constructorOpt.ifPresent(javaClass::removeMethod);
      javaClass.addAnnotation(Builder.class);
      javaClass.addAnnotation(Jacksonized.class);
    }
  }

  private static String generateEnum(String name, Schema<?> schema) {
    JavaEnumSource javaEnum = Roaster.create(JavaEnumSource.class);
    addPackageInfo(name, schema, javaEnum);
    fillEnumSourceBySchema(javaEnum, schema);
    JAVA_SOURCE_MAP.put(name, javaEnum);
    return javaEnum.toString();
  }

  private static String generateInterface(String name, Schema<?> schema, Components components) {
    if (JAVA_SOURCE_MAP.containsKey(name)) {
      return JAVA_SOURCE_MAP.get(name).toString();
    }
    JavaInterfaceSource javaInterface = Roaster.create(JavaInterfaceSource.class);
    addPackageInfo(name, schema, javaInterface);
    javaInterface.addAnnotation(JsonTypeInfo.class).setEnumValue("use", JsonTypeInfo.Id.DEDUCTION);
    JAVA_SOURCE_MAP.put(name, javaInterface);
    fillInterfaceSourceBySchema(javaInterface, schema, components);
    return javaInterface.toString();
  }

  private static void addPackageInfo(String name, Schema<?> schema, JavaSource<?> javaSource) {
    if (ObjectUtils.isEmpty(name)) {
      name = schema.getTitle();
    }
    javaSource.setPackage(DEFAULT_PACKAGE_NAME).setName(name);
  }
}
