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

public class JsObjStrictSpecConstructorsTest {

  @Test
  public void shouldValidateStrictConstructorWith50Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("k01",
                                  number,
                                  "k02",
                                  number,
                                  "k03",
                                  number,
                                  "k04",
                                  number,
                                  "k05",
                                  number,
                                  "k06",
                                  number,
                                  "k07",
                                  number,
                                  "k08",
                                  number,
                                  "k09",
                                  number,
                                  "k10",
                                  number,
                                  "k11",
                                  number,
                                  "k12",
                                  number,
                                  "k13",
                                  number,
                                  "k14",
                                  number,
                                  "k15",
                                  number,
                                  "k16",
                                  number,
                                  "k17",
                                  number,
                                  "k18",
                                  number,
                                  "k19",
                                  number,
                                  "k20",
                                  number,
                                  "k21",
                                  number,
                                  "k22",
                                  number,
                                  "k23",
                                  number,
                                  "k24",
                                  number,
                                  "k25",
                                  number,
                                  "k26",
                                  number,
                                  "k27",
                                  number,
                                  "k28",
                                  number,
                                  "k29",
                                  number,
                                  "k30",
                                  number,
                                  "k31",
                                  number,
                                  "k32",
                                  number,
                                  "k33",
                                  number,
                                  "k34",
                                  number,
                                  "k35",
                                  number,
                                  "k36",
                                  number,
                                  "k37",
                                  number,
                                  "k38",
                                  number,
                                  "k39",
                                  number,
                                  "k40",
                                  number,
                                  "k41",
                                  number,
                                  "k42",
                                  number,
                                  "k43",
                                  number,
                                  "k44",
                                  number,
                                  "k45",
                                  number,
                                  "k46",
                                  number,
                                  "k47",
                                  number,
                                  "k48",
                                  number,
                                  "k49",
                                  number,
                                  "k50",
                                  number
                                 );

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.empty();
    for (int i = 1; i <= 50; i++) {
      obj = obj.set("k%02d".formatted(i),
                    ONE);
    }

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());
  }

  @Test
  public void shouldValidateStrictConstructorWith20Args() {

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
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith19Args() {

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
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith18Args() {

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
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith17Args() {

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
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith16Args() {

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
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());

  }

  @Test
  public void shouldValidateStrictConstructorWith15Args() {

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
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith14Args() {

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
                                 );

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
    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith13Args() {

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
                                 );

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
    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }


  @Test
  public void shouldValidateStrictConstructorWith12Args() {

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
                                 );

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
    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith11Args() {

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
                                 );

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
    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith10Args() {
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
                                 );

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
    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith9Args() {
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
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());

  }

  @Test
  public void shouldValidateStrictConstructorWith8Args() {

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
                                 );

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
    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith6Args() {
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
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith5Args() {
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
                                 );

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
    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith4Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number,
                                  "d",
                                  number
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }

  @Test
  public void shouldValidateStrictConstructorWith3Args() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number,
                                  "c",
                                  number
                                 );

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

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());

  }

  @Test
  public void shouldValidateStrictConstructorWith2Args() {
    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number,
                                  "b",
                                  number
                                 );

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());

    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());

  }

  @Test
  public void shouldValidateStrictConstructorWith1Arg() {

    JsSpec number = JsSpecs.integer();
    JsObjSpec spec = JsObjSpec.of("a",
                                  number
                                 );

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE
                        );

    List<SpecError> test = spec.test(obj);
    Assertions.assertTrue(test.isEmpty());
    Assertions.assertFalse(spec.test(obj.set("z",
                                             ONE))
                               .isEmpty());


  }
}
