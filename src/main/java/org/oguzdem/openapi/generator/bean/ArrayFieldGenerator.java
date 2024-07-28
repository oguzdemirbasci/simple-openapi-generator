package org.oguzdem.json.bean;

import static org.oguzdem.json.PojoGenerator.BEAN_VALIDATION_ANNOTATIONS_ENABLED;
import static org.oguzdem.json.utils.JavaClassSourceUtils.isObject;
import static org.oguzdem.json.utils.JavaClassSourceUtils.isRef;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.swagger.util.PrimitiveType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.oguzdem.json.TypeGenerator;
import org.oguzdem.json.utils.NameUtils;

/**
 * Field generator for the array field. The field is a list or set of objects. The field is
 * annotated with @{@link Singular}. The field is also annotated with {@code @Size(min = minItems,
 * max = maxItems)} if minItems or maxItems are not null. The field is added to the constructor if
 * it is not already added.
 *
 * @author Oguz Demirbasci
 */
@Slf4j
public final class ArrayFieldGenerator extends FieldGenerator {

  private static final String LIST_TYPE_TEMPLATE = "ImmutableList<%s>";
  private static final String SET_TYPE_TEMPLATE = "ImmutableSet<%s>";

  /**
   * Constructor for the ArrayFieldGenerator.
   *
   * @param rootJavaClassSource The root java class source object
   * @param javaClassSource The java class source object
   * @param name The name of the field
   * @param schema The schema object
   * @param isRequired The required flag (whether field name is included in the required list)
   * @param components The components object as look up for referenced schemas
   */
  public ArrayFieldGenerator(
      JavaClassSource rootJavaClassSource,
      JavaClassSource javaClassSource,
      String name,
      Schema<?> schema,
      Boolean isRequired,
      Components components) {
    super(rootJavaClassSource, javaClassSource, name, schema, isRequired, components);
  }

  /**
   * Generate the field for the array. The field is a list or set of objects. The field is annotated
   * with @{@link Singular}. The field is also annotated with {@code @Size(min = minItems, max =
   * maxItems)} if minItems or maxItems are not null. The field is added to the constructor if it is
   * not already added.
   *
   * @return {@link PropertySource} object of the generated field
   */
  @Override
  public PropertySource<JavaClassSource> generateField() {
    Schema<?> itemsSchema = schema.getItems();
    String internalClass = getInternalObjectType(itemsSchema);
    if (StringUtils.isBlank(internalClass)) {
      internalClass = "Object";
    }
    String typeName;
    if (Objects.nonNull(schema.getUniqueItems()) && schema.getUniqueItems()) {
      typeName = SET_TYPE_TEMPLATE.formatted(internalClass);
      javaClassSource.addImport(ImmutableSet.class);
      rootJavaClassSource.addImport(ImmutableSet.class);
    } else {
      typeName = LIST_TYPE_TEMPLATE.formatted(internalClass);
      javaClassSource.addImport(ImmutableList.class);
      rootJavaClassSource.addImport(ImmutableList.class);
    }

    PropertySource<JavaClassSource> propertySource = javaClassSource.addProperty(typeName, name);
    FieldSource<JavaClassSource> fieldSource = propertySource.getField();
    fieldSource.getType().getTypeArguments();
    fieldSource.addAnnotation(Singular.class).setStringValue(name);
    rootJavaClassSource.addImport(Singular.class);
    addStandardAnnotations(propertySource);
    addConstructorParameter();

    if (BEAN_VALIDATION_ANNOTATIONS_ENABLED) {
      if (ObjectUtils.isNotEmpty(this.schema.getMinItems())) {
        if (!fieldSource.hasAnnotation(Size.class)) {
          fieldSource.addAnnotation(Size.class);
          rootJavaClassSource.addImport(Size.class);
        }
        fieldSource
            .getAnnotation(Size.class)
            .setLiteralValue("min", this.schema.getMinItems().toString());
      }
      if (ObjectUtils.isNotEmpty(this.schema.getMaxItems())) {
        if (!fieldSource.hasAnnotation(Size.class)) {
          fieldSource.addAnnotation(Size.class);
          rootJavaClassSource.addImport(Size.class);
        }
        fieldSource
            .getAnnotation(Size.class)
            .setLiteralValue("max", this.schema.getMaxItems().toString());
      }
    }
    return propertySource;
  }

  @Override
  String getDefaultInitializerValue(
      Object defaultObject, FieldSource<JavaClassSource> fieldSource) {
    // Todo: it should be tested for arrays
    return "";
  }

  /**
   * Get the internal object type of the array. If the array is an array of objects, the object type
   *
   * @param schema The schema object of the array
   * @return The internal object type of the array
   */
  private String getInternalObjectType(Schema<?> schema) {

    if (StringUtils.isNotBlank(schema.getType()) && schema.getType().equals("array")) {
      return Objects.nonNull(schema.getUniqueItems()) && schema.getUniqueItems()
          ? SET_TYPE_TEMPLATE.formatted(getInternalObjectType(schema.getItems()))
          : LIST_TYPE_TEMPLATE.formatted(getInternalObjectType(schema.getItems()));
    } else if (isObject(schema)) {
      return TypeGenerator.getOrGenerateType(
          NameUtils.toJavaTypeNameFormat(name), schema, components);
    } else if (isRef(schema)) {
      String referencedObjectName =
          NameUtils.toJavaTypeNameFormat(
              StringUtils.removeStart(schema.get$ref(), Components.COMPONENTS_SCHEMAS_REF));
      if (javaClassSource.getName().equals(referencedObjectName)) {
        return referencedObjectName;
      }
      return TypeGenerator.getOrGenerateType(referencedObjectName, schema, components);
    }

    PrimitiveType itemsType = PrimitiveType.fromName(schema.getType());
    if (Objects.nonNull(itemsType)) {
      return itemsType.getKeyClass().getName();
    }
    return null;
  }
}
