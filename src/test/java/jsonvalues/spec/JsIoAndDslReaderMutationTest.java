package jsonvalues.spec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import jsonvalues.JsArray;
import jsonvalues.JsInt;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsIoAndDslReaderMutationTest {

  @Test
  public void shouldRejectNullWhenParsingObjOrArrayFromStreamWithCustomParser() {
    JsParser nullParser = JsParsers.INSTANCE.ofConstant(JsNull.NULL);

    JsParserException objError =
        Assertions.assertThrows(JsParserException.class,
                                () -> JsIO.INSTANCE.parseToJsObj(new ByteArrayInputStream("null".getBytes(StandardCharsets.UTF_8)),
                                                                 nullParser));
    Assertions.assertTrue(objError.getMessage()
                                  .contains(ParserErrors.EXPECTING_FOR_OBJ_START));

    JsParserException arrayError =
        Assertions.assertThrows(JsParserException.class,
                                () -> JsIO.INSTANCE.parseToJsArray(new ByteArrayInputStream("null".getBytes(StandardCharsets.UTF_8)),
                                                                   nullParser));
    Assertions.assertTrue(arrayError.getMessage()
                                    .contains(ParserErrors.EXPECTING_FOR_ARRAY_START));
  }

  @Test
  public void shouldSerializeBothObjAndArrayIntoOutputStream() {
    ByteArrayOutputStream objOut = new ByteArrayOutputStream();
    JsIO.INSTANCE.serialize(JsObj.of("a",
                                     JsInt.of(1)),
                            objOut);
    Assertions.assertEquals("{\"a\":1}",
                            objOut.toString(StandardCharsets.UTF_8));

    ByteArrayOutputStream arrOut = new ByteArrayOutputStream();
    JsIO.INSTANCE.serialize(JsArray.of(1,
                                       2,
                                       3),
                            arrOut);
    Assertions.assertEquals("[1,2,3]",
                            arrOut.toString(StandardCharsets.UTF_8));
  }

  @Test
  public void shouldReportNotEndOfStreamWhenBytesInputStillHasUnreadData() throws JsParserException {
    DslJsReader reader = JsIO.INSTANCE.newReader("12".getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    Assertions.assertFalse(reader.isEndOfStream());
  }

  @Test
  public void shouldNotResizeCharBufferWhenPrepareBufferLengthEqualsCapacity() throws JsParserException {
    byte[] sixtyFourDigits = "0123456789012345678901234567890123456789012345678901234567890123".getBytes(StandardCharsets.UTF_8);
    DslJsReader reader = JsIO.INSTANCE.newReader(sixtyFourDigits);
    char[] before = reader.chars;
    char[] parsed = reader.prepareBuffer(0,
                                         64);
    Assertions.assertSame(before,
                          parsed);
    Assertions.assertSame(before,
                          reader.chars);
    Assertions.assertEquals('0',
                            parsed[0]);
    Assertions.assertEquals('3',
                            parsed[63]);
  }

  @Test
  public void shouldRollbackFromMarkAtStartWithoutCorruptingLastToken() throws JsParserException {
    DslJsReader reader = JsIO.INSTANCE.newReader("1".getBytes(StandardCharsets.UTF_8));
    reader.setMark();
    reader.rollbackToMark();
    Assertions.assertEquals('1',
                            (char) reader.readNextToken());
  }

  @Test
  public void shouldParseUnicodeEscapeAndRejectInvalidHexInString() throws JsParserException {
    DslJsReader ok = JsIO.INSTANCE.newReader("\"\\u0031\"".getBytes(StandardCharsets.UTF_8));
    ok.readNextToken();
    Assertions.assertEquals("1",
                            ok.readString());

    DslJsReader bad = JsIO.INSTANCE.newReader("\"\\u00G1\"".getBytes(StandardCharsets.UTF_8));
    bad.readNextToken();
    JsParserException invalidHex = Assertions.assertThrows(JsParserException.class,
                                                           bad::readString);
    Assertions.assertTrue(invalidHex.getMessage()
                                    .contains(ParserErrors.INVALID_HEX));
  }

  @Test
  public void shouldValidateTrueAndFalseConstantsBoundaries() throws JsParserException {
    DslJsReader t = JsIO.INSTANCE.newReader("true".getBytes(StandardCharsets.UTF_8));
    t.readNextToken();
    Assertions.assertTrue(t.wasTrue());

    DslJsReader shortTrue = JsIO.INSTANCE.newReader("tru".getBytes(StandardCharsets.UTF_8));
    shortTrue.readNextToken();
    JsParserException invalidTrue = Assertions.assertThrows(JsParserException.class,
                                                            shortTrue::wasTrue);
    Assertions.assertTrue(invalidTrue.getMessage()
                                     .contains(ParserErrors.INVALID_TRUE_CONSTANT));

    DslJsReader f = JsIO.INSTANCE.newReader("false".getBytes(StandardCharsets.UTF_8));
    f.readNextToken();
    Assertions.assertTrue(f.wasFalse());

    DslJsReader shortFalse = JsIO.INSTANCE.newReader("fals".getBytes(StandardCharsets.UTF_8));
    shortFalse.readNextToken();
    JsParserException invalidFalse = Assertions.assertThrows(JsParserException.class,
                                                             shortFalse::wasFalse);
    Assertions.assertTrue(invalidFalse.getMessage()
                                      .contains(ParserErrors.INVALID_FALSE_CONSTANT));
  }

  @Test
  public void shouldResetThreadLocalReaderAcrossSequentialArrayParses() {
    JsArray first = JsIO.INSTANCE.parseToJsArray("[1]".getBytes(StandardCharsets.UTF_8));
    JsArray second = JsIO.INSTANCE.parseToJsArray("[2,3]".getBytes(StandardCharsets.UTF_8));
    Assertions.assertEquals(JsArray.of(1),
                            first);
    Assertions.assertEquals(JsArray.of(2,
                                       3),
                            second);
  }

  @Test
  public void shouldResetThreadLocalReaderAcrossSequentialObjParsesWithCustomParser() {
    JsParser parser = JsParsers.INSTANCE.ofObj(false);
    JsObj first = JsIO.INSTANCE.parseToJsObj("{\"a\":1}".getBytes(StandardCharsets.UTF_8),
                                             parser);
    JsObj second = JsIO.INSTANCE.parseToJsObj("{\"b\":\"x\"}".getBytes(StandardCharsets.UTF_8),
                                              parser);
    Assertions.assertEquals(JsObj.of("a",
                                     JsInt.of(1)),
                            first);
    Assertions.assertEquals(JsObj.of("b",
                                     JsStr.of("x")),
                            second);
  }

  @Test
  public void shouldUsePublicObjParseSerializeAndPrettyApis() {
    JsObj parsed = JsIO.INSTANCE.parseToJsObj("{\"a\":1}".getBytes(StandardCharsets.UTF_8));
    Assertions.assertEquals(JsObj.of("a",
                                     JsInt.of(1)),
                            parsed);

    byte[] serialized = JsIO.INSTANCE.serialize(JsArray.of(4,
                                                           5));
    Assertions.assertArrayEquals("[4,5]".getBytes(StandardCharsets.UTF_8),
                                 serialized);

    String pretty = JsIO.INSTANCE.toPrettyString(JsObj.of("a",
                                                           JsInt.of(1),
                                                           "b",
                                                           JsArray.of(2,
                                                                      3)),
                                                 2);
    Assertions.assertTrue(pretty.contains("\n"));
    Assertions.assertTrue(pretty.contains("\"a\""));
    Assertions.assertTrue(pretty.contains("\"b\""));
  }

  @Test
  public void shouldRespectExactDigitBufferLimitWhenPreparingBuffer() throws JsParserException {
    JsIO io = new JsIO(new Settings().limitDigitsBuffer(64));
    byte[] sixtyFourDigits = "0123456789012345678901234567890123456789012345678901234567890123".getBytes(StandardCharsets.UTF_8);
    DslJsReader reader = io.newReader(sixtyFourDigits);

    Assertions.assertDoesNotThrow(() -> reader.prepareBuffer(0,
                                                             64));

    JsParserException tooManyDigits =
        Assertions.assertThrows(JsParserException.class,
                                () -> reader.prepareBuffer(0,
                                                           65));
    Assertions.assertTrue(tooManyDigits.getMessage()
                                       .contains("Too many digits"));
  }

  @Test
  public void shouldSkipMultibyteUnicodeWhitespaces() throws JsParserException {
    // U+2000 EN QUAD encoded as E2 80 80
    byte[] enQuadThenDigit = new byte[] {(byte) 0xE2, (byte) 0x80, (byte) 0x80, '1'};
    DslJsReader reader = JsIO.INSTANCE.newReader(enQuadThenDigit);
    Assertions.assertEquals('1',
                            (char) reader.readNextToken());
  }

  @Test
  public void shouldSupportUpperAndLowerHexBoundariesInUnicodeEscapes() throws JsParserException {
    DslJsReader upperA = JsIO.INSTANCE.newReader("\"\\u0041\"".getBytes(StandardCharsets.UTF_8));
    upperA.readNextToken();
    Assertions.assertEquals("A",
                            upperA.readString());

    DslJsReader upperF = JsIO.INSTANCE.newReader("\"\\u0046\"".getBytes(StandardCharsets.UTF_8));
    upperF.readNextToken();
    Assertions.assertEquals("F",
                            upperF.readString());

    DslJsReader lowerA = JsIO.INSTANCE.newReader("\"\\u0061\"".getBytes(StandardCharsets.UTF_8));
    lowerA.readNextToken();
    Assertions.assertEquals("a",
                            lowerA.readString());

    DslJsReader lowerF = JsIO.INSTANCE.newReader("\"\\u0066\"".getBytes(StandardCharsets.UTF_8));
    lowerF.readNextToken();
    Assertions.assertEquals("f",
                            lowerF.readString());

    DslJsReader digitNine = JsIO.INSTANCE.newReader("\"\\u0039\"".getBytes(StandardCharsets.UTF_8));
    digitNine.readNextToken();
    Assertions.assertEquals("9",
                            digitNine.readString());
  }

  @Test
  public void shouldSkipAdditionalUnicodeWhitespaceFamilies() throws JsParserException {
    // U+1680 OGHAM SPACE MARK => E1 9A 80
    DslJsReader ogham = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE1, (byte) 0x9A, (byte) 0x80, '7'});
    Assertions.assertEquals('7',
                            (char) ogham.readNextToken());

    // U+205F MEDIUM MATHEMATICAL SPACE => E2 81 9F
    DslJsReader mediumMath = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE2, (byte) 0x81, (byte) 0x9F, '8'});
    Assertions.assertEquals('8',
                            (char) mediumMath.readNextToken());

    // U+3000 IDEOGRAPHIC SPACE => E3 80 80
    DslJsReader ideographic = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE3, (byte) 0x80, (byte) 0x80, '9'});
    Assertions.assertEquals('9',
                            (char) ideographic.readNextToken());
  }

  @Test
  public void shouldNotTreatIncompleteUnicodeWhitespacePrefixesAsWhitespace() throws JsParserException {
    DslJsReader incompleteE1 = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE1});
    Assertions.assertEquals((byte) 0xE1,
                            incompleteE1.readNextToken());

    DslJsReader incompleteE3 = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE3, (byte) 0x80});
    Assertions.assertEquals((byte) 0xE3,
                            incompleteE3.readNextToken());
  }

  @Test
  public void shouldFailWithParserExceptionWhenInputStreamReadFailsDuringSetup() {
    InputStream broken = new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException("boom");
      }
    };
    JsParser parser = JsParsers.INSTANCE.ofObj(false);

    Assertions.assertThrows(JsParserException.class,
                            () -> JsIO.INSTANCE.parseToJsObj(broken,
                                                             parser));

    InputStream broken2 = new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException("boom");
      }
    };
    JsParser arrayParser = JsParsers.INSTANCE.ofConstant(JsNull.NULL);
    Assertions.assertThrows(JsParserException.class,
                            () -> JsIO.INSTANCE.parseToJsArray(broken2,
                                                               arrayParser));
  }

  @Test
  public void shouldRejectUnterminatedAndPrematureEndedStrings() throws JsParserException {
    DslJsReader premature = JsIO.INSTANCE.newReader("\"".getBytes(StandardCharsets.UTF_8));
    premature.readNextToken();
    JsParserException prematureEnd = Assertions.assertThrows(JsParserException.class,
                                                             premature::readString);
    Assertions.assertTrue(prematureEnd.getMessage()
                                      .contains(ParserErrors.PREMATURE_END_OF_JSONSTRING));

    DslJsReader unclosed = JsIO.INSTANCE.newReader("\"abc".getBytes(StandardCharsets.UTF_8));
    unclosed.readNextToken();
    JsParserException notClosed = Assertions.assertThrows(JsParserException.class,
                                                          unclosed::readString);
    Assertions.assertTrue(notClosed.getMessage()
                                   .contains(ParserErrors.STRING_NOT_CLOSED));
  }

  @Test
  public void shouldRejectInvalidEscapeCharacterInString() throws JsParserException {
    DslJsReader invalidEscape = JsIO.INSTANCE.newReader("\"\\x\"".getBytes(StandardCharsets.UTF_8));
    invalidEscape.readNextToken();
    JsParserException error = Assertions.assertThrows(JsParserException.class,
                                                      invalidEscape::readString);
    Assertions.assertTrue(error.getMessage()
                               .contains("Invalid escape combination detected"));
  }

  @Test
  public void shouldRejectInvalidUnicodeSequenceWhenParsingLongString() throws JsParserException {
    byte[] payload = new byte[1 + 64 + 5 + 1];
    int idx = 0;
    payload[idx++] = '"';
    for (int i = 0; i < 64; i++) {
      payload[idx++] = 'a';
    }
    payload[idx++] = (byte) 0xF8;
    payload[idx++] = (byte) 0x88;
    payload[idx++] = (byte) 0x80;
    payload[idx++] = (byte) 0x80;
    payload[idx++] = (byte) 0x80;
    payload[idx] = '"';

    DslJsReader reader = JsIO.INSTANCE.newReader(payload);
    reader.readNextToken();
    JsParserException invalid = Assertions.assertThrows(JsParserException.class,
                                                        reader::readString);
    Assertions.assertTrue(invalid.getMessage()
                                 .contains(ParserErrors.INVALID_UNICODE_CHARACTER));
  }

  @Test
  public void shouldEnforceMaximumStringBufferLimit() throws JsParserException {
    JsIO limited = new JsIO(new Settings().limitStringBuffer(64));
    String tooLong = "\"" + "a".repeat(70) + "\"";
    DslJsReader reader = limited.newReader(tooLong.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();

    JsParserException tooLarge = Assertions.assertThrows(JsParserException.class,
                                                         reader::readString);
    Assertions.assertTrue(tooLarge.getMessage()
                                  .contains("Maximum string buffer limit exceeded"));
  }

  @Test
  public void shouldReadLongStringAcrossSmallStreamingBuffer() throws JsParserException {
    String value = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ__";
    String json = "\"" + value + "\"";
    DslJsReader reader = JsIO.INSTANCE.newReader(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                                                 new byte[8]);
    reader.readNextToken();
    Assertions.assertEquals(value,
                            reader.readString());
    Assertions.assertTrue(reader.isEndOfStream());
  }

  @Test
  public void shouldRollbackRestoreLastTokenWhenMarkIsAfterFirstToken() throws JsParserException {
    DslJsReader reader = JsIO.INSTANCE.newReader("[1]".getBytes(StandardCharsets.UTF_8));
    reader.readNextToken(); // [
    reader.setMark();
    reader.readNextToken(); // 1
    reader.rollbackToMark();

    Assertions.assertEquals('[',
                            (char) reader.last());
    Assertions.assertEquals('1',
                            (char) reader.readNextToken());
  }

  @Test
  public void shouldReturnFalseFromWasFalseWhenLastIsNotF() throws JsParserException {
    DslJsReader reader = JsIO.INSTANCE.newReader("true".getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    Assertions.assertFalse(reader.wasFalse());
  }

  @Test
  public void shouldNotTreatNearUnicodeWhitespaceAsWhitespace() throws JsParserException {
    DslJsReader nearE2 = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE2, (byte) 0x81, (byte) 0xA0});
    Assertions.assertEquals((byte) 0xE2,
                            nearE2.readNextToken());

    DslJsReader nearE3 = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE3, (byte) 0x80, (byte) 0x81});
    Assertions.assertEquals((byte) 0xE3,
                            nearE3.readNextToken());
  }

  @Test
  public void shouldDecodeHexEscapesUsingUpperAndLowerAlphaDigits() throws JsParserException {
    DslJsReader upper = JsIO.INSTANCE.newReader("\"\\u00AF\"".getBytes(StandardCharsets.UTF_8));
    upper.readNextToken();
    Assertions.assertEquals("\u00AF",
                            upper.readString());

    DslJsReader lower = JsIO.INSTANCE.newReader("\"\\u00af\"".getBytes(StandardCharsets.UTF_8));
    lower.readNextToken();
    Assertions.assertEquals("\u00af",
                            lower.readString());

    DslJsReader mixed = JsIO.INSTANCE.newReader("\"\\uF0a1\"".getBytes(StandardCharsets.UTF_8));
    mixed.readNextToken();
    Assertions.assertEquals("\uF0A1",
                            mixed.readString());
  }

  @Test
  public void shouldRejectWhenEscapedStringExpansionExceedsConfiguredMaximum() throws JsParserException {
    JsIO limited = new JsIO(new Settings().limitStringBuffer(128));
    String payload = "\"" + "a".repeat(122) + "\\u0041\"";
    DslJsReader reader = limited.newReader(payload.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();

    JsParserException tooLarge = Assertions.assertThrows(JsParserException.class,
                                                         reader::readString);
    Assertions.assertTrue(tooLarge.getMessage()
                                  .contains("Maximum string buffer limit exceeded"));
  }

  @Test
  public void shouldRejectWhenMultibyteStringExpansionExceedsConfiguredMaximum() throws JsParserException {
    JsIO limited = new JsIO(new Settings().limitStringBuffer(128));
    String payload = "\"" + "a".repeat(124) + "€\"";
    DslJsReader reader = limited.newReader(payload.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();

    JsParserException tooLarge = Assertions.assertThrows(JsParserException.class,
                                                         reader::readString);
    Assertions.assertTrue(tooLarge.getMessage()
                                  .contains("Maximum string buffer limit exceeded"));
  }

  @Test
  public void shouldDecodeTwoThreeAndFourByteUtf8AfterFastPathBoundary() throws JsParserException {
    String payload = "\"" + "a".repeat(64) + "ñ€😀\"";
    DslJsReader reader = JsIO.INSTANCE.newReader(payload.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    Assertions.assertEquals("a".repeat(64) + "ñ€😀",
                            reader.readString());
  }

  @Test
  public void shouldRejectCodePointsAboveUnicodeMaximumWhenDecodingLongString() throws JsParserException {
    byte[] bytes = new byte[1 + 64 + 4 + 1];
    int i = 0;
    bytes[i++] = '"';
    for (int p = 0; p < 64; p++) {
      bytes[i++] = 'a';
    }
    bytes[i++] = (byte) 0xF4;
    bytes[i++] = (byte) 0x90;
    bytes[i++] = (byte) 0x80;
    bytes[i++] = (byte) 0x80;
    bytes[i] = '"';

    DslJsReader reader = JsIO.INSTANCE.newReader(bytes);
    reader.readNextToken();
    JsParserException invalid = Assertions.assertThrows(JsParserException.class,
                                                        reader::readString);
    Assertions.assertTrue(invalid.getMessage()
                                 .contains(ParserErrors.INVALID_UNICODE_CHARACTER));
  }

  @Test
  public void shouldNotPrepareNextBlockWhenCurrentIndexEqualsReadLimit() throws JsParserException {
    byte[] data = "123".getBytes(StandardCharsets.UTF_8);
    InputStream stream = new InputStream() {
      private int calls = 0;

      @Override
      public int read(byte[] b,
                      int off,
                      int len) throws IOException {
        calls++;
        if (calls == 1) {
          System.arraycopy(data,
                           0,
                           b,
                           off,
                           data.length);
          return data.length;
        }
        if (calls == 2) {
          return -1;
        }
        throw new IOException("unexpected refill");
      }

      @Override
      public int read() {
        return -1;
      }
    };

    DslJsReader reader = JsIO.INSTANCE.newReader(stream,
                                                 new byte[40]);
    Assertions.assertEquals('1',
                            (char) reader.readNextToken());
    Assertions.assertEquals('2',
                            (char) reader.readNextToken());
    Assertions.assertEquals('3',
                            (char) reader.readNextToken());
  }

  @Test
  public void shouldRejectIncompleteAndUnknownE2WhitespaceSequences() throws JsParserException {
    DslJsReader incompleteE2 = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE2});
    Assertions.assertEquals((byte) 0xE2,
                            incompleteE2.readNextToken());

    DslJsReader unknownE280 = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE2, (byte) 0x80, (byte) 0xA0});
    Assertions.assertEquals((byte) 0xE2,
                            unknownE280.readNextToken());
  }

  @Test
  public void shouldDecodeUnicodeEscapeWhenSecondNibbleIsNonZero() throws JsParserException {
    DslJsReader reader = JsIO.INSTANCE.newReader("\"\\u0FA1\"".getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    Assertions.assertEquals("\u0FA1",
                            reader.readString());
  }

  @Test
  public void shouldDecodeFourByteUtf8WithNonZeroLowBits() throws JsParserException {
    String wink = "\uD83D\uDE09";
    String payload = "\"" + "a".repeat(64) + wink + "\"";
    DslJsReader reader = JsIO.INSTANCE.newReader(payload.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    Assertions.assertEquals("a".repeat(64) + wink,
                            reader.readString());
  }

  @Test
  public void shouldDecodeCodePointAtUnicodeSupplementaryBoundary() throws JsParserException {
    String cp10000 = "\uD800\uDC00";
    String payload = "\"" + "a".repeat(64) + cp10000 + "\"";
    DslJsReader reader = JsIO.INSTANCE.newReader(payload.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    Assertions.assertEquals("a".repeat(64) + cp10000,
                            reader.readString());
  }

  @Test
  public void shouldAllowFirstStringGrowthWhenLimitEqualsNextCapacity() throws JsParserException {
    JsIO limited = new JsIO(new Settings().limitStringBuffer(128));
    String payload = "\"" + "a".repeat(65) + "\"";
    DslJsReader reader = limited.newReader(payload.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    Assertions.assertEquals("a".repeat(65),
                            reader.readString());
  }

  @Test
  public void shouldAllowSecondLoopGrowthAtExactCapacityBoundary() throws JsParserException {
    JsIO limited = new JsIO(new Settings().limitStringBuffer(128));
    String payload = "\"" + "\\\"" + "a".repeat(64) + "\"";
    DslJsReader reader = limited.newReader(payload.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    Assertions.assertEquals("\"" + "a".repeat(64),
                            reader.readString());
  }

  @Test
  public void shouldHandleTruncatedUnicodeWhitespaceLeadsWithoutSkipping() throws JsParserException {
    DslJsReader truncatedE1 = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE1, (byte) 0x9A});
    Assertions.assertEquals((byte) 0xE1,
                            truncatedE1.readNextToken());

    DslJsReader truncatedE2 = JsIO.INSTANCE.newReader(new byte[] {(byte) 0xE2, (byte) 0x81});
    Assertions.assertEquals((byte) 0xE2,
                            truncatedE2.readNextToken());
  }
}
