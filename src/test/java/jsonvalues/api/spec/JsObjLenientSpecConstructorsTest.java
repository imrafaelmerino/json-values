package jsonvalues.api.spec;

import java.util.List;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import jsonvalues.spec.SpecError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsObjLenientSpecConstructorsTest {

  @Test
  public void shouldValidateLenientConstructorWith20Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number,
                                  "l",
                                  number,
                                  "m",
                                  number,
                                  "n",
                                  number,
                                  "o",
                                  number,
                                  "p",
                                  number,
                                  "q",
                                  number,
                                  "r",
                                  number,
                                  "s",
                                  number,
                                  "t",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE,
                         "l",
                         ONE,
                         "m",
                         ONE,
                         "n",
                         ONE,
                         "o",
                         ONE,
                         "p",
                         ONE,
                         "q",
                         ONE,
                         "r",
                         ONE,
                         "s",
                         ONE,
                         "t",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);

    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith19Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number,
                                  "l",
                                  number,
                                  "m",
                                  number,
                                  "n",
                                  number,
                                  "o",
                                  number,
                                  "p",
                                  number,
                                  "q",
                                  number,
                                  "r",
                                  number,
                                  "s",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE,
                         "l",
                         ONE,
                         "m",
                         ONE,
                         "n",
                         ONE,
                         "o",
                         ONE,
                         "p",
                         ONE,
                         "q",
                         ONE,
                         "r",
                         ONE,
                         "s",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);

    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith18Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number,
                                  "l",
                                  number,
                                  "m",
                                  number,
                                  "n",
                                  number,
                                  "o",
                                  number,
                                  "p",
                                  number,
                                  "q",
                                  number,
                                  "r",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE,
                         "l",
                         ONE,
                         "m",
                         ONE,
                         "n",
                         ONE,
                         "o",
                         ONE,
                         "p",
                         ONE,
                         "q",
                         ONE,
                         "r",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);

    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith17Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number,
                                  "l",
                                  number,
                                  "m",
                                  number,
                                  "n",
                                  number,
                                  "o",
                                  number,
                                  "p",
                                  number,
                                  "q",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE,
                         "l",
                         ONE,
                         "m",
                         ONE,
                         "n",
                         ONE,
                         "o",
                         ONE,
                         "p",
                         ONE,
                         "q",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);

    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith16Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number,
                                  "l",
                                  number,
                                  "m",
                                  number,
                                  "n",
                                  number,
                                  "o",
                                  number,
                                  "p",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE,
                         "l",
                         ONE,
                         "m",
                         ONE,
                         "n",
                         ONE,
                         "o",
                         ONE,
                         "p",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);

    Assertions.assertTrue(test.isEmpty());
    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith15Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number,
                                  "l",
                                  number,
                                  "m",
                                  number,
                                  "n",
                                  number,
                                  "o",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE,
                         "l",
                         ONE,
                         "m",
                         ONE,
                         "n",
                         ONE,
                         "o",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);

    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith14Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number,
                                  "l",
                                  number,
                                  "m",
                                  number,
                                  "n",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE,
                         "l",
                         ONE,
                         "m",
                         ONE,
                         "n",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith13Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number,
                                  "l",
                                  number,
                                  "m",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE,
                         "l",
                         ONE,
                         "m",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }


  @Test
  public void shouldValidateLenientConstructorWith12Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number,
                                  "l",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE,
                         "l",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith11Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number,
                                  "k",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE,
                         "k",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith10Args() {
    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number,
                                  "i",
                                  number,
                                  "j",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE,
                         "i",
                         ONE,
                         "j",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith9Args() {
    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number,
                                  "h",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE,
                         "h",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());

  }

  @Test
  public void shouldValidateLenientConstructorWith8Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number,
                                  "g",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE,
                         "g",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith6Args() {
    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number,
                                  "f",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE,
                         "f",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith5Args() {
    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number,
                                  "e",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE,
                         "e",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith4Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE,
                         "d",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }

  @Test
  public void shouldValidateLenientConstructorWith3Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());

  }

  @Test
  public void shouldValidateLenientConstructorWith2Args() {
    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());

    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());

  }

  @Test
  public void shouldValidateLenientConstructorWith1Arg() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number
                                 )
                              .lenient();

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertTrue(spec.test(obj.set("z",
                                            ONE))
                              .isEmpty());


  }
}
