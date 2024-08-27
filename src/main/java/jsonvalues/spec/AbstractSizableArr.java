package jsonvalues.spec;

abstract class AbstractSizableArr extends AbstractNullable {

  ArraySchemaConstraints arrayConstraints;

  protected AbstractSizableArr(boolean nullable,
                               ArraySchemaConstraints arrayConstraints) {
    super(nullable);
    this.arrayConstraints = arrayConstraints;
  }

  protected AbstractSizableArr(boolean nullable) {
    super(nullable);

  }
}
