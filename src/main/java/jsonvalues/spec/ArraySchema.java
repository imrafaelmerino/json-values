package jsonvalues.spec;

/**
 * A class representing the schema for arrays in a JSON structure. It allows setting constraints such as minimum and
 * maximum number of items and uniqueness of items in the array.
 */
public final class ArraySchema {

  private ArraySchema() {
  }

  private int minItems;
  private int maxItems;
  private boolean uniqueItems;

  /**
   * Sets the minimum number of items allowed in the array.
   *
   * @param minItems The minimum number of items (inclusive). Must be greater than or equal to 0.
   * @return This ArraySchema instance for method chaining.
   * @throws IllegalArgumentException If minItems is negative.
   */
  public static ArraySchema withMinSize(int minItems) {
    if (minItems < 0) {
      throw new IllegalArgumentException("minItems must be >= 0");
    }
    var schema = new ArraySchema();
    schema.minItems = minItems;
    return schema;
  }


  public static ArraySchema withMaxSize(int maxItems) {
    if (maxItems < 0) {
      throw new IllegalArgumentException("maxItems must be >= 0");
    }
    var schema = new ArraySchema();
    schema.maxItems = maxItems;
    return schema;
  }

  public static ArraySchema sizeBetween(int minItems,
                                        int maxItems) {
    if (minItems < 0) {
      throw new IllegalArgumentException("minItems must be >= 0");
    }
    if (maxItems < 0) {
      throw new IllegalArgumentException("maxItems must be >= 0");
    }
    var schema = new ArraySchema();
    schema.minItems = minItems;
    schema.maxItems = maxItems;
    return schema;
  }


  /**
   * Sets the flag to enforce unique items in the array.
   *
   * @return This ArraySchema instance for method chaining.
   */
  public ArraySchema setUniqueItems() {
    this.uniqueItems = true;
    return this;
  }

  /**
   * Builds and returns an instance of ArraySchemaConstraints based on the specified constraints.
   *
   * @return An instance of ArraySchemaConstraints with the specified constraints.
   */
  ArraySchemaConstraints build() {
    return new ArraySchemaConstraints(minItems,
                                      maxItems,
                                      uniqueItems);
  }
}
