package org.oguzdem.openapi.generator.bean;

import static io.swagger.v3.parser.util.SchemaTypeUtil.BINARY_FORMAT;
import static io.swagger.v3.parser.util.SchemaTypeUtil.BOOLEAN_TYPE;
import static io.swagger.v3.parser.util.SchemaTypeUtil.BYTE_FORMAT;
import static io.swagger.v3.parser.util.SchemaTypeUtil.DATE_FORMAT;
import static io.swagger.v3.parser.util.SchemaTypeUtil.DATE_TIME_FORMAT;
import static io.swagger.v3.parser.util.SchemaTypeUtil.FLOAT_FORMAT;
import static io.swagger.v3.parser.util.SchemaTypeUtil.INTEGER32_FORMAT;
import static io.swagger.v3.parser.util.SchemaTypeUtil.INTEGER64_FORMAT;
import static io.swagger.v3.parser.util.SchemaTypeUtil.INTEGER_TYPE;
import static io.swagger.v3.parser.util.SchemaTypeUtil.NUMBER_TYPE;
import static io.swagger.v3.parser.util.SchemaTypeUtil.OBJECT_TYPE;
import static io.swagger.v3.parser.util.SchemaTypeUtil.STRING_TYPE;
import static io.swagger.v3.parser.util.SchemaTypeUtil.UUID_FORMAT;

import com.google.common.base.Strings;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 * Factory class to create a new instance of the {@link FieldGenerator}.
 *
 * @author Oguz Demirbasci
 */
@Slf4j
public class FieldGeneratorFactory {

  /**
   * Null generator is used to generate null fields. It is used when the schema is not recognized.
   */
  public static final FieldGenerator NULL_GENERATOR =
      new FieldGenerator(null, null, "null", null, false, null) {
        @Override
        public PropertySource<JavaClassSource> generateField() {
          return null;
        }

        @Override
        String getDefaultInitializerValue(
            Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
          return null;
        }
      };

  /**
   * Factory method to create a new instance of the {@link Builder}.
   *
   * @return a new instance of the {@link Builder}
   */
  public static Builder factory() {
    return new Builder();
  }

  /**
   * Create a new instance of the {@link FieldGenerator} by the given schema type.
   *
   * @param rootJavaClassSource the root java class source
   * @param javaClassSource the java class source
   * @param name the name of the field
   * @param schema the schema object
   * @param isRequired the required flag (whether field name is included in the required list)
   * @param components the components object as look up for referenced schemas
   * @return a new instance of the {@link FieldGenerator}
   */
  private static FieldGenerator createGeneratorByType(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {

    return switch (Strings.nullToEmpty(schema.getType())) {
      case INTEGER_TYPE ->
          switch (Strings.nullToEmpty(schema.getFormat())) {
            case INTEGER64_FORMAT ->
                new LongFieldGenerator(
                    rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
            default ->
                new IntFieldGenerator(
                    rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
          };
      case NUMBER_TYPE ->
          switch (Strings.nullToEmpty(schema.getFormat())) {
            case INTEGER32_FORMAT ->
                new IntFieldGenerator(
                    rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
            case INTEGER64_FORMAT ->
                new LongFieldGenerator(
                    rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
            case FLOAT_FORMAT ->
                new FloatFieldGenerator(
                    rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
            default ->
                new DoubleFieldGenerator(
                    rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
          };
      case STRING_TYPE -> {
        if (Objects.nonNull(schema.getEnum())) {
          yield new EnumFieldGenerator(
              rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
        }
        yield switch (Strings.nullToEmpty(schema.getFormat())) {
          case DATE_FORMAT ->
              new DateFieldGenerator(
                  rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
          case DATE_TIME_FORMAT ->
              new DateTimeFieldGenerator(
                  rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
          case BYTE_FORMAT, BINARY_FORMAT ->
              new ByteArrayFieldGenerator(
                  rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
          case UUID_FORMAT ->
              new UuidFieldGenerator(
                  rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
          case "uri" ->
              new UriFieldGenerator(
                  rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
          case "url" ->
              new UrlFieldGenerator(
                  rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
          default ->
              new StringFieldGenerator(
                  rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
        };
      }
      case BOOLEAN_TYPE ->
          new BooleanFieldGenerator(
              rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
      case "array" ->
          new ArrayFieldGenerator(
              rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
      case OBJECT_TYPE ->
          new ObjectFieldGenerator(
              rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
      default -> {
        if (Objects.nonNull(schema.get$ref())) {
          yield new RefFieldGenerator(
              rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
        } else {
          yield NULL_GENERATOR;
        }
      }
    };
  }

  /**
   * Create a new instance of the {@link FieldGenerator} for the additional properties. If the
   * schema has additional properties, create a new instance of the {@link
   * AdditionalPropertyFieldGenerator}. Otherwise, return the {@link #NULL_GENERATOR}.
   *
   * @param rootJavaClassSource the root java class source
   * @param javaClassSource the java class source
   * @param schema the schema object
   * @param components the components object as look up for referenced schemas
   * @return a new instance of the {@link FieldGenerator}
   */
  private static FieldGenerator createAdditionalPropsGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      Schema<?> schema,
      Components components) {
    if (Objects.equals(schema.getAdditionalProperties(), Boolean.TRUE)) {
      return new AdditionalPropertyFieldGenerator(
          rootJavaClassSource, javaClassSource, schema, components);
    }
    return NULL_GENERATOR;
  }

  /** Builder class to create a new instance of the {@link FieldGenerator}. */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Builder {
    private JavaClassSource rootJavaClassSource;
    private JavaClassSource javaClassSource;
    private String name;
    private Schema<?> schema;
    private Boolean isRequired;
    private Components components;

    public Builder javaClassSource(JavaClassSource javaClassSource) {
      this.javaClassSource = javaClassSource;
      return this;
    }

    public Builder rootClassSource(JavaClassSource rootJavaClassSource) {
      this.rootJavaClassSource = rootJavaClassSource;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder schema(Schema<?> schema) {
      this.schema = schema;
      return this;
    }

    public Builder isRequired(Boolean isRequired) {
      this.isRequired = isRequired;
      return this;
    }

    public Builder components(Components components) {
      this.components = components;
      return this;
    }

    /**
     * Build a new instance of the {@link FieldGenerator}.
     *
     * @return a new instance of the {@link FieldGenerator}
     */
    public FieldGenerator build() {
      return createGeneratorByType(
          rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
    }

    /**
     * Build a new instance of the {@link FieldGenerator} for the additional properties.
     *
     * @return a new instance of the {@link FieldGenerator}
     */
    public FieldGenerator buildAdditionalPropertiesGenerator() {
      return createAdditionalPropsGenerator(
          rootJavaClassSource, javaClassSource, schema, components);
    }
  }
}
