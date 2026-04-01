package jsonvalues.spec;

import java.nio.charset.StandardCharsets;
import jsonvalues.JsInt;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DslJsReaderAndJsParsersTest {

  private final JsIO io = JsIO.INSTANCE;

  @Test
  public void shouldRollbackWithoutMarkThrows() {
    DslJsReader reader = io.newReader("{}".getBytes(StandardCharsets.UTF_8));
    Assertions.assertThrows(IllegalArgumentException.class,
                            reader::rollbackToMark);
  }

  @Test
  public void shouldSetMarkAndRollback() throws JsParserException {
    DslJsReader reader = io.newReader("[1]".getBytes(StandardCharsets.UTF_8));
    reader.readNextToken(); // [
    reader.setMark();
    byte token = reader.readNextToken(); // 1
    Assertions.assertEquals('1',
                            (char) token);

    reader.rollbackToMark();
    token = reader.readNextToken();
    Assertions.assertEquals('1',
                            (char) token);
  }

  @Test
  public void shouldProcessRejectsLengthGreaterThanBuffer() {
    DslJsReader reader = io.newReader(new byte[2]);
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> reader.process(new byte[2],
                                                 3));
  }

  @Test
  public void shouldReadKeyRequiresColon() {
    DslJsReader reader = io.newReader("\"a\" 1".getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    JsParserException e = Assertions.assertThrows(JsParserException.class,
                                                  reader::readKey);
    Assertions.assertTrue(e.getMessage()
                           .contains(ParserErrors.EXPECTING_COLON));
  }

  @Test
  public void shouldWasNullTrueAndInvalidNull() throws JsParserException {
    DslJsReader ok = io.newReader("null".getBytes(StandardCharsets.UTF_8));
    ok.readNextToken();
    Assertions.assertTrue(ok.wasNull());

    DslJsReader invalid = io.newReader("nuxx".getBytes(StandardCharsets.UTF_8));
    invalid.readNextToken();
    JsParserException e = Assertions.assertThrows(JsParserException.class,
                                                  invalid::wasNull);
    Assertions.assertTrue(e.getMessage()
                           .contains(ParserErrors.INVALID_NULL));
  }

  @Test
  public void shouldWasTrueAndWasFalseInvalid() throws JsParserException {
    DslJsReader t = io.newReader("true".getBytes(StandardCharsets.UTF_8));
    t.readNextToken();
    Assertions.assertTrue(t.wasTrue());

    DslJsReader badFalse = io.newReader("fauxe".getBytes(StandardCharsets.UTF_8));
    badFalse.readNextToken();
    JsParserException e = Assertions.assertThrows(JsParserException.class,
                                                  badFalse::wasFalse);
    Assertions.assertTrue(e.getMessage()
                           .contains(ParserErrors.INVALID_FALSE_CONSTANT));
  }

  @Test
  public void shouldCheckArrayEndErrors() throws JsParserException {
    DslJsReader unexpectedEnd = io.newReader("[".getBytes(StandardCharsets.UTF_8));
    unexpectedEnd.readNextToken();
    JsParserException endError = Assertions.assertThrows(JsParserException.class,
                                                         unexpectedEnd::checkArrayEnd);
    Assertions.assertTrue(endError.getMessage()
                                  .contains(ParserErrors.UNEXPECTED_END_OF_ARRAY));

    DslJsReader expectingBracket = io.newReader("[1}".getBytes(StandardCharsets.UTF_8));
    expectingBracket.readNextToken(); // [
    expectingBracket.readNextToken(); // 1
    JsParserException bracketError = Assertions.assertThrows(JsParserException.class,
                                                             expectingBracket::checkArrayEnd);
    Assertions.assertTrue(bracketError.getMessage()
                                      .contains(ParserErrors.EXPECTING_END_OF_ARRAY));
  }

  @Test
  public void shouldOfConstantAndOfValueSuchThat() throws JsParserException {
    JsParser constantNull = JsParsers.INSTANCE.ofConstant(JsNull.NULL);
    DslJsReader nullReader = io.newReader("null".getBytes(StandardCharsets.UTF_8));
    nullReader.readNextToken();
    Assertions.assertEquals(JsNull.NULL,
                            constantNull.parse(nullReader));

    JsParser constantOne = JsParsers.INSTANCE.ofConstant(JsInt.of(1));
    DslJsReader wrongReader = io.newReader("null".getBytes(StandardCharsets.UTF_8));
    wrongReader.readNextToken();
    JsParserException nullNotExpected =
        Assertions.assertThrows(JsParserException.class,
                                () -> constantOne.parse(wrongReader));
    Assertions.assertTrue(nullNotExpected.getMessage()
                                         .contains(ERROR_CODE.NULL_NOT_EXPECTED.name()));

    DslJsReader mismatchReader = io.newReader("2".getBytes(StandardCharsets.UTF_8));
    mismatchReader.readNextToken();
    JsParserException constantMismatch =
        Assertions.assertThrows(JsParserException.class,
                                () -> constantOne.parse(mismatchReader));
    Assertions.assertTrue(constantMismatch.getMessage()
                                          .contains(ERROR_CODE.CONSTANT_CONDITION.name()));

    JsParser valuePredicate =
        JsParsers.INSTANCE.ofValueSuchThat(v -> v.equals(JsInt.of(7)) ?
                                                null :
                                                new JsError(v,
                                                            ERROR_CODE.VALUE_CONDITION));
    DslJsReader okReader = io.newReader("7".getBytes(StandardCharsets.UTF_8));
    okReader.readNextToken();
    Assertions.assertEquals(JsInt.of(7),
                            valuePredicate.parse(okReader));
  }

  @Test
  public void shouldOfMapOfStringWithConstraints() throws JsParserException {
    JsParser parser = JsParsers.INSTANCE.ofMapOfString(false,
                                                       new StrConstraints(2,
                                                                          10,
                                                                          null,
                                                                          null));
    DslJsReader invalid = io.newReader("{\"a\":\"x\"}".getBytes(StandardCharsets.UTF_8));
    invalid.readNextToken();
    JsParserException e = Assertions.assertThrows(JsParserException.class,
                                                  () -> parser.parse(invalid));
    Assertions.assertTrue(e.getMessage()
                           .contains(ParserErrors.STR_LENGTH_LOWER_THAN_MINIMUM));

    DslJsReader valid = io.newReader("{\"a\":\"xy\"}".getBytes(StandardCharsets.UTF_8));
    valid.readNextToken();
    JsValue value = parser.parse(valid);
    Assertions.assertEquals(JsObj.of("a",
                                     JsStr.of("xy")),
                            value);
  }
}
