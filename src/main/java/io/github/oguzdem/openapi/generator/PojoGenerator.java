package io.github.oguzdem.openapi.generator;

import static io.github.oguzdem.openapi.generator.utils.JavaClassSourceUtils.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.oguzdem.openapi.generator.utils.NameUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import joptsimple.internal.Strings;
import lombok.Builder;
import lombok.Generated;
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
  public static <T> JavaSource<?> generate(
      String name, @NonNull Schema<T> schema, Components components) {
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
    } else if (isArray(schema)) {
      return generateArrayClass(className, schema, components);
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
    File theDir = new File(Config.getFullOutputPath());
    if (!theDir.exists()) {
      theDir.mkdirs();
    }

    JAVA_SOURCE_MAP.forEach(
        (name, javaSource) -> {
          try (PrintWriter out =
              new PrintWriter(Config.getFullOutputPath() + "/%s.java".formatted(name))) {
            out.println(javaSource);
          } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  private static <T> JavaSource<JavaClassSource> generateArrayClass(
      String name, @NonNull Schema<T> schema, Components components) {
    if (!isArray(schema)) {
      return null;
    }

    String subType = TypeGenerator.getOrGenerateType(Strings.EMPTY, schema.getItems(), components);

    JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
    javaClass.setSuperType("ArrayList<%s>".formatted(subType));
    addJavadoc(schema, javaClass);
    addPackageInfo(name, schema, javaClass);
    JAVA_SOURCE_MAP.put(name, javaClass);
    javaClass.addImport(ArrayList.class);
    javaClass.addAnnotation(Generated.class);
    return javaClass;
  }

  private static <T> JavaSource<JavaClassSource> generateClass(
      String name, @NonNull Schema<T> schema, Components components) {
    JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
    addJavadoc(schema, javaClass);
    addPackageInfo(name, schema, javaClass);
    fillJavaClassSourceBySchema(javaClass, schema, components);
    adjustConstructorByDefaultParams(javaClass, schema);
    JAVA_SOURCE_MAP.put(name, javaClass);
    javaClass.addAnnotation(Generated.class);
    return javaClass;
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

  private static JavaSource<JavaEnumSource> generateEnum(String name, Schema<?> schema) {
    JavaEnumSource javaEnum = Roaster.create(JavaEnumSource.class);
    addJavadoc(schema, javaEnum);
    addPackageInfo(name, schema, javaEnum);
    fillEnumSourceBySchema(javaEnum, schema);
    JAVA_SOURCE_MAP.put(name, javaEnum);
    javaEnum.addAnnotation(Generated.class);
    return javaEnum;
  }

  private static JavaSource<?> generateInterface(
      String name, Schema<?> schema, Components components) {
    if (JAVA_SOURCE_MAP.containsKey(name)) {
      return JAVA_SOURCE_MAP.get(name);
    }
    JavaInterfaceSource javaInterface = Roaster.create(JavaInterfaceSource.class);
    addJavadoc(schema, javaInterface);
    addPackageInfo(name, schema, javaInterface);
    javaInterface.addAnnotation(JsonTypeInfo.class).setEnumValue("use", JsonTypeInfo.Id.DEDUCTION);
    JAVA_SOURCE_MAP.put(name, javaInterface);
    fillInterfaceSourceBySchema(javaInterface, schema, components);
    javaInterface.addAnnotation(Generated.class);
    return javaInterface;
  }

  private static void addPackageInfo(String name, Schema<?> schema, JavaSource<?> javaSource) {
    if (ObjectUtils.isEmpty(name)) {
      name = schema.getTitle();
    }
    javaSource.setPackage(Config.getPackageName()).setName(name);
  }

  private static void addJavadoc(Schema<?> schema, JavaSource<?> javaSource) {
    if (ObjectUtils.isEmpty(schema)) {
      return;
    }
    StringBuilder javadocBuilder = new StringBuilder();
    if (StringUtils.isNotBlank(schema.getDescription())) {
      javadocBuilder.append("Description: ");
      javadocBuilder.append(schema.getDescription());
      if (!schema.getDescription().endsWith(".")) {
        javadocBuilder.append(".");
      }
    }
    if (ObjectUtils.isNotEmpty(schema.getExternalDocs())) {
      if (ObjectUtils.isEmpty(schema.getExternalDocs().getUrl())
          && ObjectUtils.isNotEmpty(schema.getExternalDocs().getDescription())) {
        if (!javadocBuilder.isEmpty()) {
          javadocBuilder.append("\n<p>");
        }
        javadocBuilder.append("Refer to: %s".formatted(schema.getExternalDocs().getDescription()));
      } else if (ObjectUtils.isNotEmpty(schema.getExternalDocs().getUrl())) {
        String label = schema.getExternalDocs().getUrl();
        if (ObjectUtils.isNotEmpty(schema.getExternalDocs().getDescription())) {
          label = schema.getExternalDocs().getDescription();
        }
        if (!javadocBuilder.isEmpty()) {
          javadocBuilder.append("\n<p>");
        }
        javadocBuilder.append(
            "Refer to: <a href=\"%s\">%s</a>".formatted(schema.getExternalDocs().getUrl(), label));
      }
    }
    if (ObjectUtils.isNotEmpty(schema.getExample())) {
      if (!javadocBuilder.isEmpty()) {
        javadocBuilder.append("\n<p>");
      }
      javadocBuilder.append("Example: \n<pre>");
      ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
      try {
        javadocBuilder.append(
            "\n{@code\n %s\n} ".formatted(objectWriter.writeValueAsString(schema.getExample())));
      } catch (JsonProcessingException e) {
        javadocBuilder.append("\n %s".formatted(schema.getExample()));
      }

      javadocBuilder.append("\n</pre>");
    }
    javaSource.getJavaDoc().setFullText(javadocBuilder.toString());
  }
}
