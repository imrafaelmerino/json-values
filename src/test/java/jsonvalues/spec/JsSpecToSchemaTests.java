package jsonvalues.spec;

import org.junit.jupiter.api.Test;

public class JsSpecToSchemaTests {

  JsObjSpecToSchema schema = new JsObjSpecToSchema();

  @Test
  public void test() {
    JsObjSpec objSpec = JsObjSpec.of("name",
                                     JsSpecs.str(),
                                     "age",
                                     JsSpecs.integer(),
                                     "address",
                                     JsObjSpec.of("street",
                                                  JsSpecs.str(),
                                                  "city",
                                                  JsSpecs.str(),
                                                  "zip",
                                                  JsSpecs.integer()
                                                 ),
                                     "vip",
                                     JsSpecs.bool(),
                                     "height",
                                     JsSpecs.decimal(),
                                     "distance",
                                     JsSpecs.longInteger(),
                                     "image",
                                     JsSpecs.binary(),
                                     "birthDate",
                                     JsSpecs.instant()
                                    );

    System.out.println(schema.apply(objSpec)
                             .toPrettyString());

  }

}
