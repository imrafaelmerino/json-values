package jsonvalues.api;

import static jsonvalues.JsPath.path;

import jsonvalues.JsInt;
import jsonvalues.JsObj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsObjConstructorsTest {


  @Test
  public void shouldSupportConstructorWith15Args() {

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

    Assertions.assertEquals(15,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith14Args() {

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

    Assertions.assertEquals(14,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith13Args() {

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

    Assertions.assertEquals(13,
                            obj.size());


  }


  @Test
  public void shouldSupportConstructorWith12Args() {

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

    Assertions.assertEquals(12,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith11Args() {

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

    Assertions.assertEquals(11,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith10Args() {

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

    Assertions.assertEquals(10,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith9Args() {

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
                         ONE
                        );

    Assertions.assertEquals(9,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith8Args() {

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

    Assertions.assertEquals(8,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith7Args() {

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

    Assertions.assertEquals(7,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith6Args() {

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

    Assertions.assertEquals(6,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith5Args() {

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

    Assertions.assertEquals(5,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith4Args() {

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

    Assertions.assertEquals(4,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith3Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE,
                         "c",
                         ONE
                        );

    Assertions.assertEquals(3,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith2Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE,
                         "b",
                         ONE
                        );

    Assertions.assertEquals(2,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith1Arg() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of("a",
                         ONE
                        );

    Assertions.assertEquals(1,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith15Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE,
                         path("/l"),
                         ONE,
                         path("/m"),
                         ONE,
                         path("/n"),
                         ONE,
                         path("/o"),
                         ONE);

    Assertions.assertEquals(15,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith14Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE,
                         path("/l"),
                         ONE,
                         path("/m"),
                         ONE,
                         path("/n"),
                         ONE);

    Assertions.assertEquals(14,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith13Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE,
                         path("/l"),
                         ONE,
                         path("/m"),
                         ONE);

    Assertions.assertEquals(13,
                            obj.size());


  }


  @Test
  public void shouldSupportPathConstructorWith12Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE,
                         path("/l"),
                         ONE);

    Assertions.assertEquals(12,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith11Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE);

    Assertions.assertEquals(11,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith10Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE);
    Assertions.assertEquals(10,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith9Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE);

    Assertions.assertEquals(9,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith8Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE
                        );

    Assertions.assertEquals(8,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith7Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE
                        );

    Assertions.assertEquals(7,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith6Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE
                        );

    Assertions.assertEquals(6,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith5Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE
                        );

    Assertions.assertEquals(5,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith4Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE
                        );

    Assertions.assertEquals(4,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith3Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE
                        );

    Assertions.assertEquals(3,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith2Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE
                        );

    Assertions.assertEquals(2,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith1Arg() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE
                        );

    Assertions.assertEquals(1,
                            obj.size());


  }


  @Test
  public void shouldSupportConstructorWith16Args() {

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

    Assertions.assertEquals(16,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith17Args() {

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

    Assertions.assertEquals(17,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith18Args() {

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

    Assertions.assertEquals(18,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith19Args() {

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

    Assertions.assertEquals(19,
                            obj.size());


  }

  @Test
  public void shouldSupportConstructorWith20Args() {

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

    Assertions.assertEquals(20,
                            obj.size());


  }


  @Test
  public void shouldSupportPathConstructorWith16Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE,
                         path("/l"),
                         ONE,
                         path("/m"),
                         ONE,
                         path("/n"),
                         ONE,
                         path("/o"),
                         ONE,
                         path("/p"),
                         ONE);

    Assertions.assertEquals(16,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith17Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE,
                         path("/l"),
                         ONE,
                         path("/m"),
                         ONE,
                         path("/n"),
                         ONE,
                         path("/o"),
                         ONE,
                         path("/p"),
                         ONE,
                         path("/q"),
                         ONE);

    Assertions.assertEquals(17,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith18Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE,
                         path("/l"),
                         ONE,
                         path("/m"),
                         ONE,
                         path("/n"),
                         ONE,
                         path("/o"),
                         ONE,
                         path("/p"),
                         ONE,
                         path("/q"),
                         ONE,
                         path("/r"),
                         ONE);

    Assertions.assertEquals(18,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith19Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE,
                         path("/l"),
                         ONE,
                         path("/m"),
                         ONE,
                         path("/n"),
                         ONE,
                         path("/o"),
                         ONE,
                         path("/p"),
                         ONE,
                         path("/q"),
                         ONE,
                         path("/r"),
                         ONE,
                         path("/s"),
                         ONE);

    Assertions.assertEquals(19,
                            obj.size());


  }

  @Test
  public void shouldSupportPathConstructorWith20Args() {

    JsInt ONE = JsInt.of(1);
    JsObj obj = JsObj.of(path("/a"),
                         ONE,
                         path("/b"),
                         ONE,
                         path("/c"),
                         ONE,
                         path("/d"),
                         ONE,
                         path("/e"),
                         ONE,
                         path("/f"),
                         ONE,
                         path("/g"),
                         ONE,
                         path("/h"),
                         ONE,
                         path("/i"),
                         ONE,
                         path("/j"),
                         ONE,
                         path("/k"),
                         ONE,
                         path("/l"),
                         ONE,
                         path("/m"),
                         ONE,
                         path("/n"),
                         ONE,
                         path("/o"),
                         ONE,
                         path("/p"),
                         ONE,
                         path("/q"),
                         ONE,
                         path("/r"),
                         ONE,
                         path("/s"),
                         ONE,
                         path("/t"),
                         ONE);

    Assertions.assertEquals(20,
                            obj.size());


  }


}

