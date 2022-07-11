package com.dslplatform.json;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NumberConverterTest {

    private final DslJson<Object> dslJson = MyDslJson.INSTANCE;

    @Test
    public void rangeCheckInt() throws IOException {
        // setup
        final JsonWriter sw = new JsonWriter(40,
                                             null);
        final JsonReader<Object> jr = dslJson.newReader(sw.getByteBuffer());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        final int from = -10000000;
        final int to = 10000000;

        for (int value = from; value <= to; value += 33) {
            sw.reset();

            // serialization
            MyNumberConverter.serialize(value,
                                        sw);

            jr.process(null,
                       sw.size());
            jr.read();
            final int valueParsed1 = MyNumberConverter.deserializeInt(jr);
            Assertions.assertEquals(value,
                                    valueParsed1);

            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final int valueParsed2 = MyNumberConverter.deserializeInt(jsr);
            Assertions.assertEquals(value,
                                    valueParsed2);
        }
    }

    @Test
    public void rangeCheckLong() throws IOException {
        // setup
        final JsonWriter sw = new JsonWriter(40,
                                             null);
        final JsonReader<Object> jr = dslJson.newReader(sw.getByteBuffer());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        final long from = -10000000000L;
        final long to = 10000000000L;

        for (long value = from; value <= to; value += 33333) {
            sw.reset();

            // serialization
            MyNumberConverter.serialize(value,
                                        sw);

            jr.process(null,
                       sw.size());
            jr.read();
            final long valueParsed1 = MyNumberConverter.deserializeLong(jr);
            Assertions.assertEquals(value,
                                    valueParsed1);

            jr.process(null,
                       sw.size());
            jr.read();
            final Number numberParsed1 = MyNumberConverter.deserializeNumber(jr);
            Assertions.assertEquals(value,
                                    numberParsed1);

            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final long valueParsed2 = MyNumberConverter.deserializeLong(jsr);
            Assertions.assertEquals(value,
                                    valueParsed2);

            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final Number numberParsed2 = MyNumberConverter.deserializeNumber(jsr);
            Assertions.assertEquals(value,
                                    numberParsed2);
        }
    }

    @Test
    public void rangeCheckDecimal() throws IOException {
        // setup
        final JsonWriter sw = new JsonWriter(40,
                                             null);
        final JsonReader<Object> jr = dslJson.newReader(sw.getByteBuffer());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        final int from = -100000000;
        final int to = 100000000;
        final double[] dividers = {1, 10, 0.1, 100, 0.01, 1000, 0.001};
        int x = 0;

        for (int value = from; value <= to; value += 333) {
            sw.reset();

            // serialization
            BigDecimal bd = BigDecimal.valueOf(value / dividers[x++ % dividers.length]);
            MyNumberConverter.serialize(bd,
                                        sw);

            jr.process(null,
                       sw.size());
            jr.read();
            final BigDecimal valueParsed1 = MyNumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(bd,
                                    valueParsed1);
            jr.process(null,
                       sw.size());
            jr.read();
            final Number numberParsed1 = MyNumberConverter.deserializeNumber(jr);
            Assertions.assertEquals(bd,
                                    BigDecimal.valueOf(numberParsed1.doubleValue()));

            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final BigDecimal valueParsed2 = MyNumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(bd,
                                    valueParsed2);
            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final Number numberParsed2 = MyNumberConverter.deserializeNumber(jsr);
            Assertions.assertEquals(bd,
                                    BigDecimal.valueOf(numberParsed2.doubleValue()));
        }
    }


    @Test
    public void testSerialization() {
        // setup
        final JsonWriter sw = new JsonWriter(40,
                                             null);

        final int from = -1000000;
        final int to = 1000000;

        for (long value = from; value <= to; value++) {

            // init
            sw.reset();

            // serialization
            MyNumberConverter.serialize(value,
                                        sw);

            // check
            final String valueString = sw.toString();
            final int valueParsed = Integer.parseInt(valueString);
            Assertions.assertEquals(value,
                                    valueParsed);
        }
    }


    @Test
    public void testPowersOf10() throws IOException {
        String sciForm = "1";

        final int maxLen = Long.toString(Long.MAX_VALUE).length();
        for (int i = 0; i < maxLen; i++) {
            // space to prevent end of stream gotcha
            final byte[] body = (sciForm + " ").getBytes(StandardCharsets.ISO_8859_1);

            final JsonReader<Object> jr = dslJson.newReader(body);
            jr.getNextToken();
            final long parsed1 = MyNumberConverter.deserializeLong(jr);
            jr.process(new byte[64],
                       64);
            jr.process(new ByteArrayInputStream(body));
            jr.getNextToken();
            final long parsed2 = MyNumberConverter.deserializeLong(jr);

            final long check = Long.parseLong(sciForm);
            Assertions.assertEquals(check,
                                    parsed1);
            Assertions.assertEquals(check,
                                    parsed2);

            sciForm += '0';
        }
    }

    @Test
    public void testGenericNumber() throws IOException {
        String input = "{\"coordinates\": [{\n" +
                "      \"x\": 0.7497682823992804,\n" +
                "      \"y\": 0.11430576315631691,\n" +
                "      \"z\": 0.8336834710515213,\n" +
                "      \"id\": \"1804\",\n" +
                "      \"conf\": {\"1\": [1,true]}\n" +
                "    },\n" +
                "    {\n" +
                "      \"x\": 0.996765457871507,\n" +
                "      \"y\": 0.7250564959301626,\n" +
                "      \"z\": 0.4599639911379607,\n" +
                "      \"id\": \"2546\",\n" +
                "      \"conf\": {\"1\": [1,true]\n" +
                "      }\n" +
                "    }]}";
        Map result = (Map) dslJson.deserialize(Map.class,
                                            input.getBytes(),
                                            input.length());
        Assertions.assertNotNull(result);
    }

    @Test
    public void testGenericNumberLongBoundaries() throws IOException {
        final Long maxIntAsLong = Long.valueOf(Integer.MAX_VALUE);
        final Long minIntAsLong = Long.valueOf(Integer.MIN_VALUE);
        final BigDecimal maxIntWithDecimalAsBigDecimal = BigDecimal.valueOf(Integer.MAX_VALUE).setScale(1);
        final BigDecimal minIntWithDecimalAsBigDecimal = BigDecimal.valueOf(Integer.MIN_VALUE).setScale(1);
        final Long positive18DigitLong = 876543210987654321L;
        final Long negative18DigitLong = -876543210987654321L;
        final BigDecimal positive18DigitAndOneDecimal = BigDecimal.valueOf(876543210987654321L).setScale(1);
        final BigDecimal negative18DigitAndOneDecimal = BigDecimal.valueOf(-876543210987654321L).setScale(1);
        final Long maxLong = Long.MAX_VALUE;
        final Long minLong = Long.MIN_VALUE;
        final BigDecimal maxLongPlusOneAsBigDecimal = BigDecimal.valueOf(Long.MAX_VALUE).add(BigDecimal.ONE);
        final BigDecimal minLongMinusOneAsBigDecimal = BigDecimal.valueOf(Long.MIN_VALUE).subtract(BigDecimal.ONE);

        String input = "{\n" +
                "\"maxIntAsLong\":" + maxIntAsLong + ",\n" +
                "\"minIntAsLong\":" + minIntAsLong + ",\n" +
                "\"maxIntWithDecimalAsBigDecimal\":" + maxIntWithDecimalAsBigDecimal + ",\n" +
                "\"minIntWithDecimalAsBigDecimal\":" + minIntWithDecimalAsBigDecimal + ",\n" +
                "\"positive18DigitLong\":" + positive18DigitLong + ",\n" +
                "\"negative18DigitLong\":" + negative18DigitLong + ",\n" +
                "\"positive18DigitAndOneDecimal\":" + positive18DigitAndOneDecimal + ",\n" +
                "\"negative18DigitAndOneDecimal\":" + negative18DigitAndOneDecimal + ",\n" +
                "\"maxLong\":" + maxLong + ",\n" +
                "\"minLong\":" + minLong + ",\n" +
                "\"maxLongPlusOneAsBigDecimal\":" + maxLongPlusOneAsBigDecimal + ",\n" +
                "\"minLongMinusOneAsBigDecimal\":" + minLongMinusOneAsBigDecimal + "\n" +
                "}";

        DslJson json = new DslJson();
        Map result = (Map) json.deserialize(Map.class,
                                            input.getBytes("UTF-8"),
                                            input.length());
        Assertions.assertEquals(maxIntAsLong,
                                result.get("maxIntAsLong"));
        Assertions.assertEquals(minIntAsLong,
                                result.get("minIntAsLong"));
        Assertions.assertEquals(maxIntWithDecimalAsBigDecimal,
                                result.get("maxIntWithDecimalAsBigDecimal"));
        Assertions.assertEquals(minIntWithDecimalAsBigDecimal,
                                result.get("minIntWithDecimalAsBigDecimal"));
        Assertions.assertEquals(positive18DigitLong,
                                result.get("positive18DigitLong"));
        Assertions.assertEquals(negative18DigitLong,
                                result.get("negative18DigitLong"));
        Assertions.assertEquals(positive18DigitAndOneDecimal,
                                result.get("positive18DigitAndOneDecimal"));
        Assertions.assertEquals(negative18DigitAndOneDecimal,
                                result.get("negative18DigitAndOneDecimal"));
        Assertions.assertEquals(maxLong,
                                result.get("maxLong"));
        Assertions.assertEquals(minLong,
                                result.get("minLong"));
        Assertions.assertEquals(maxLongPlusOneAsBigDecimal,
                                result.get("maxLongPlusOneAsBigDecimal"));
        Assertions.assertEquals(minLongMinusOneAsBigDecimal,
                                result.get("minLongMinusOneAsBigDecimal"));
    }


    @Test
    public void shortWhitespaceGuard() throws IOException {
        String input = "1234  ";
        final JsonReader<Object> jr = dslJson.newReader(input.getBytes());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(input.getBytes()),
                                                         new byte[64]);
        jr.getNextToken();
        Number number = MyNumberConverter.deserializeNumber(jr);
        Assertions.assertTrue(number instanceof Long);
        jsr.getNextToken();
        number = MyNumberConverter.deserializeNumber(jsr);
        Assertions.assertTrue(number instanceof Long);
    }

    @Test
    public void longWhitespaceGuard() throws IOException {
        String input = "1234        \t\n\r               ";
        final JsonReader<Object> reader = dslJson.newReader(input.getBytes());
        reader.getNextToken();
        Number number = MyNumberConverter.deserializeNumber(reader);
        Assertions.assertTrue(number instanceof Long);
    }

    @Test
    public void overflowDetection() throws IOException {
        String input = "1234567890123456        \t\n\r               ";
        JsonReader<Object> reader = dslJson.newReader(input.getBytes());
        reader.getNextToken();
        try {
            MyNumberConverter.deserializeInt(reader);
            Assertions.fail();
        } catch (JsParserException e) {
            Assertions.assertTrue(e.getMessage().contains("Integer overflow"));
        }
        input = "-1234567890123456        \t\n\r               ";
        reader = dslJson.newReader(input.getBytes());
        reader.getNextToken();
        try {
            MyNumberConverter.deserializeInt(reader);
            Assertions.fail();
        } catch (JsParserException e) {
            Assertions.assertTrue(e.getMessage().contains("Integer overflow"));
        }
    }

    @Test
    public void doubleRandom() throws IOException {
        final JsonWriter sw = new JsonWriter(40,
                                             null);
        final JsonReader<Object> jr = dslJson.newReader(sw.getByteBuffer());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        final Random rnd = new Random(0);

        for (int i = 0; i < 1000000; i++) {
            sw.reset();

            // serialization
            BigDecimal d = BigDecimal.valueOf(rnd.nextDouble());
            MyNumberConverter.serialize(d,
                                        sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = MyNumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(d,
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = MyNumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(d,
                                    valueParsed2);
        }
    }

    @Test
    public void doubleIntRandom() throws IOException {
        final JsonWriter sw = new JsonWriter(40,
                                             null);
        final JsonReader<Object> jr = dslJson.newReader(sw.getByteBuffer());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        final Random rnd = new Random(0);

        for (int i = 0; i < 1000000; i++) {
            sw.reset();

            // serialization
            BigDecimal d = BigDecimal.valueOf((rnd.nextDouble() * rnd.nextInt()));
            MyNumberConverter.serialize(d,
                                        sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = MyNumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(d,
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = MyNumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(d,
                                    valueParsed2);
        }
    }

    @Test
    public void floatRandom() throws IOException {
        // setup
        final JsonWriter sw = new JsonWriter(40,
                                             null);
        final JsonReader<Object> jr = dslJson.newReader(sw.getByteBuffer());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        final Random rnd = new Random(0);

        for (int i = 0; i < 1000000; i++) {
            sw.reset();

            // serialization
            BigDecimal f = BigDecimal.valueOf(rnd.nextFloat());
            MyNumberConverter.serialize(f,
                                        sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = MyNumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(f,
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = MyNumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(f,
                                    valueParsed2);
        }
    }

    @Test
    public void floatIntRandom() throws IOException {
        // setup
        final JsonWriter sw = new JsonWriter(40,
                                             null);
        final JsonReader<Object> jr = dslJson.newReader(sw.getByteBuffer());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        final Random rnd = new Random(0);

        for (int i = 0; i < 1000000; i++) {
            sw.reset();

            // serialization
            BigDecimal d = BigDecimal.valueOf((float) rnd.nextDouble() * rnd.nextInt());
            MyNumberConverter.serialize(d,
                                        sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = MyNumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(d,
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = MyNumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(d,
                                    valueParsed2);
        }
    }

    private void prepareJson(JsonReader<Object> reader,
                             byte[] input) throws IOException {
        reader.process(input,
                       input.length);
        reader.read();
        reader.read();
        reader.fillName();
        reader.read();
    }


    private BigDecimal checkDecimalError(JsonReader<Object> reader,
                                         String error) {
        BigDecimal res = null;
        try {
            res = MyNumberConverter.deserializeDecimal(reader);
            if (error != null) Assertions.fail("Expecting " + error);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assertions.assertTrue(ex.getMessage().contains(error));
        }
        return res;
    }

    private int checkIntError(JsonReader<Object> reader,
                              String error) {
        int res = 0;
        try {
            res = MyNumberConverter.deserializeInt(reader);
            if (error != null) Assertions.fail("Expecting " + error);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assertions.assertTrue(ex.getMessage().contains(error));
        }
        return res;
    }

    private long checkLongError(JsonReader<Object> reader,
                                String error) {
        long res = 0;
        try {
            res = MyNumberConverter.deserializeLong(reader);
            if (error != null) Assertions.fail("Expecting " + error);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assertions.assertTrue(ex.getMessage().contains(error));
        }
        return res;
    }

    private Number checkNumberError(JsonReader<Object> reader,
                                    String error) {
        Number res = null;
        try {
            res = MyNumberConverter.deserializeNumber(reader);
            if (error != null) Assertions.fail("Expecting " + error);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assertions.assertTrue(ex.getMessage().contains(error));
        }
        return res;
    }

    @Test
    public void emptyParsing() throws IOException {
        final JsonReader<Object> jr = dslJson.newReader(new byte[0]);

        byte[] empty = "{\"x\":}".getBytes("UTF-8");
        byte[] space = "{\"x\": }".getBytes("UTF-8");
        byte[] plus = "{\"x\":+}".getBytes("UTF-8");
        byte[] minus = "{\"x\":-}".getBytes("UTF-8");
        byte[] e = "{\"x\":e}".getBytes("UTF-8");
        byte[] plusSpace = "{\"x\":+ }".getBytes("UTF-8");
        byte[] minusSpace = "{\"x\":- }".getBytes("UTF-8");
        byte[] eSpace = "{\"x\":E }".getBytes("UTF-8");
        byte[] dot = "{\"x\":.}".getBytes("UTF-8");
        byte[] doubleMinus = "{\"x\":--0}".getBytes("UTF-8");
        byte[] doubleMinusSpace = "{\"x\":--0}".getBytes("UTF-8");
        byte[] doubleZero = "{\"x\":00}".getBytes("UTF-8");
        byte[] doubleNegativeZero = "{\"x\":-00}".getBytes("UTF-8");
        byte[] doubleZeroAndSpace = "{\"x\":00 }".getBytes("UTF-8");
        byte[] leadingZero = "{\"x\":01}".getBytes("UTF-8");
        byte[] leadingNegativeZero = "{\"x\":-01}".getBytes("UTF-8");
        byte[] leadingZeroAndSpace = "{\"x\":01 }".getBytes("UTF-8");
        byte[] quotedLeadingZero = "{\"x\":\"01\"}".getBytes("UTF-8");
        byte[] quotedLeadingNegativeZero = "{\"x\":\"-01\"}".getBytes("UTF-8");
        byte[] quotedLeadingZeroAndSpace = "{\"x\":\"01\" }".getBytes("UTF-8");

        byte[][] input = {
                empty, space, plus, minus, e, plusSpace, minusSpace, eSpace, dot, doubleMinus, doubleMinusSpace,
                doubleZero, doubleNegativeZero, doubleZeroAndSpace, leadingZero, leadingNegativeZero, leadingZeroAndSpace,
                quotedLeadingZero, quotedLeadingNegativeZero, quotedLeadingZeroAndSpace
        };

        for (byte[] it : input) {
            prepareJson(jr,
                        it);
            checkDecimalError(jr,
                              "@ position");
            prepareJson(jr,
                        it);
            checkDecimalError(jr,
                              "@ position");
            prepareJson(jr,
                        it);
            checkDecimalError(jr,
                              "@ position");
            prepareJson(jr,
                        it);
            checkIntError(jr,
                          "@ position");
            prepareJson(jr,
                        it);
            checkLongError(jr,
                           "@ position");
            prepareJson(jr,
                        it);
            checkNumberError(jr,
                             "@ position");
        }
    }

    @Test
    public void zeroParsing() throws IOException {
        final JsonReader<Object> jr = dslJson.newReader(new byte[0]);

        byte[] zeroWithSpace = "{\"x\":0 }".getBytes("UTF-8");
        byte[] negativeZeroWithSpace = "{\"x\":-0 }".getBytes("UTF-8");

        byte[][] input = {zeroWithSpace, negativeZeroWithSpace};

        for (byte[] it : input) {
            prepareJson(jr,
                        it);
            Assertions.assertEquals(BigDecimal.ZERO,
                                    checkDecimalError(jr,
                                                      null));
            prepareJson(jr,
                        it);
            Assertions.assertEquals(BigDecimal.ZERO,
                                    checkDecimalError(jr,
                                                      null));
            prepareJson(jr,
                        it);
            Assertions.assertEquals(BigDecimal.ZERO,
                                    checkDecimalError(jr,
                                                      null));
            prepareJson(jr,
                        it);
            Assertions.assertEquals(0,
                                    checkIntError(jr,
                                                  null));
            prepareJson(jr,
                        it);
            Assertions.assertEquals(0L,
                                    checkLongError(jr,
                                                   null));
            prepareJson(jr,
                        it);
            Assertions.assertEquals(0,
                                    checkNumberError(jr,
                                                     null).intValue());
        }
    }

    @Test
    public void wrongSpaceParsing() throws IOException {
        final JsonReader<Object> jr = dslJson.newReader(new byte[0]);

        byte[] doubleZero = "{\"x\":0 0}".getBytes("UTF-8");
        byte[] doubleDot1 = "{\"x\":0.0.}".getBytes("UTF-8");
        byte[] doubleDot2 = "{\"x\":0..0}".getBytes("UTF-8");
        byte[] dotNoNumber1 = "{\"x\":.0}".getBytes("UTF-8");
        byte[] dotNoNumber2 = "{\"x\":0.}".getBytes("UTF-8");

        byte[][] input = {doubleZero, doubleDot1, doubleDot2, dotNoNumber1, dotNoNumber2};

        for (byte[] it : input) {
            prepareJson(jr,
                        it);
            checkDecimalError(jr,
                              "@ position=");
            prepareJson(jr,
                        it);
            checkDecimalError(jr,
                              "@ position=");
            prepareJson(jr,
                        it);
            checkDecimalError(jr,
                              "@ position=");
            prepareJson(jr,
                        it);
            checkIntError(jr,
                          "@ position=");
            prepareJson(jr,
                        it);
            checkLongError(jr,
                           "@ position=");
        }
    }

    @Test
    public void specialFloats() throws IOException {
        final JsonReader<Object> jr = dslJson.newReader(new byte[0]);
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        String[] values = {
                Double.toString(Double.MAX_VALUE / 10),
                Double.toString(Double.MIN_VALUE * 10),
                Double.toString(Double.MIN_VALUE / 10),
                Double.toString(Double.MIN_VALUE),
                Double.toString(Double.MAX_VALUE),
                "1E46",
                "1e-46",
                "0.00000000000000001",
                "0.000000000000000001",
                "0.0000000000000000001",
                "0.0000000000000000000000000000001",
                "0.00000000000000000000000000000000000000000001",
                "0.000000000000000000000000000000000000000000001",
                "0.0000000000000000000000000000000000000000000001",
                "0.7706706532754006",
                "0.7706706532754006",
                "0.77067065327",
                "0.77067065327",
                "1000000000000000000000000",
                "100000000000000000000000000000000000000",
                "1000000000000000000000000000000000000000",
                "100000000000000000000000000000000000000.000000000000000000000001",
                "100000000000000000000000000000000000000.000000000000000000000001e-10"
        };

        for (String d : values) {
            System.out.println(d);
            BigDecimal f = new BigDecimal(d);

            byte[] input = d.getBytes("UTF-8");
            jr.process(input,
                       input.length);
            jr.read();

            final BigDecimal valueParsed1 = MyNumberConverter.deserializeDecimal(jr);

            Assertions.assertTrue(f.compareTo(valueParsed1) == 0);

            final ByteArrayInputStream is = new ByteArrayInputStream(input,
                                                                     0,
                                                                     input.length);
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = MyNumberConverter.deserializeDecimal(jsr);
            System.out.println(f);
            System.out.println(valueParsed2);
            Assertions.assertTrue(f.compareTo(valueParsed2) == 0);
        }
    }

    @Test
    public void doubleRoundingError() throws IOException {
        final DslJson<Object> dslJson = new DslJson<Object>(new DslJson.Settings<Object>().doublePrecision(JsonReader.DoublePrecision.DEFAULT));
        final JsonWriter sw = new JsonWriter(40,
                                             null);
        final JsonReader<Object> jr = dslJson.newReader(sw.getByteBuffer());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        double[] values = {
                -5983259.62725876d,
                3.999999999999999555d,
                65600.45509999999d,
                65600.45509999998d,
                -707224.705947716d,
                0.04040949867001864d,
                -5983259.62725876d,
                -1800849.97476139d,
                54940.89750944923d,
                54940.89750944924d,
                //54940.897509449234d, //TODO: doesn't work in default
                -740342.9473267009d,
                -74034294.73267009d,
                -7403429.473267009d,
                -7403429.4732670095d,
                0.6374174253501083d,
                0.6374174253501084d,
                -9.514467982939291E8d,
                0.9644868606768501d,
                0.96448686067685d,
                //2.716906186888657d, //TODO: doesn't work in default
                2.7169061868886573d,
                98.48415401998089d,
                98.48415401998088d,
                -9603443.683176761d,
                7.551599396638066E8d,
                8.484850737442602E8,
                -99.86965d,
                -777.77d,
                0.984841540199809d,
                0.9848415401998091d,
                1.111538368674174E9d,
                1.1115383686741738E9d,
                0.730967787376657d,
                0.7309677873766569d,
                Double.MIN_VALUE, Double.MAX_VALUE
        };

        for (double d : values) {
            sw.reset();

            //System.out.println("processing " + d);

            MyNumberConverter.serialize(BigDecimal.valueOf(d),
                                        sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = MyNumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(BigDecimal.valueOf(d),
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = MyNumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(BigDecimal.valueOf(d),
                                    valueParsed2);
        }
    }

    @Test
    public void zeroSpaceEndParsing() throws IOException {
        final JsonReader<Object> jr = dslJson.newReader(new byte[0]);

        byte[] positive = "{\"x\":0 }".getBytes("UTF-8");
        byte[] negative = "{\"x\":-0 }".getBytes("UTF-8");

        byte[][] inputs = {positive, negative};
        for (byte[] input : inputs) {
            prepareJson(jr,
                        input);
            Assertions.assertEquals(BigDecimal.ZERO,
                                    checkDecimalError(jr,
                                                      null));
            prepareJson(jr,
                        input);
            Assertions.assertEquals(BigDecimal.ZERO,
                                    checkDecimalError(jr,
                                                      null));
            prepareJson(jr,
                        input);
            Assertions.assertEquals(BigDecimal.ZERO,
                                    checkDecimalError(jr,
                                                      null));
            prepareJson(jr,
                        input);
            Assertions.assertEquals(0,
                                    checkIntError(jr,
                                                  null));
            prepareJson(jr,
                        input);
            Assertions.assertEquals(0L,
                                    checkLongError(jr,
                                                   null));
            prepareJson(jr,
                        input);
            Assertions.assertEquals(0L,
                                    checkNumberError(jr,
                                                     null));
        }
    }

    @Test
    public void decimalSpaceEndParsing() throws IOException {
        final JsonReader<Object> jr = dslJson.newReader(new byte[0]);

        byte[] positive = "{\"x\":11.1 }".getBytes("UTF-8");
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(BigDecimal.valueOf(11.1d),
                                checkDecimalError(jr,
                                                  null));
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(BigDecimal.valueOf(11.1),
                                checkDecimalError(jr,
                                                  null));
        prepareJson(jr,
                    positive);
        checkIntError(jr,
                      "@ position");
        prepareJson(jr,
                    positive);
        checkLongError(jr,
                       "@ position");
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(BigDecimal.valueOf(11.1),
                                checkNumberError(jr,
                                                 null));

        byte[] negative = "{\"x\":-11.1 }".getBytes("UTF-8");
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(BigDecimal.valueOf(-11.1d),
                                checkDecimalError(jr,
                                                  null));
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(BigDecimal.valueOf(-11.1),
                                checkDecimalError(jr,
                                                  null));
        prepareJson(jr,
                    negative);
        checkIntError(jr,
                      "@ position");
        prepareJson(jr,
                    negative);
        checkLongError(jr,
                       "@ position");
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(BigDecimal.valueOf(-11.1),
                                checkNumberError(jr,
                                                 null));
    }

    @Test
    public void exponentSpaceEndParsing() throws IOException {
        final JsonReader<Object> jr = dslJson.newReader(new byte[0]);

        byte[] positive = "{\"x\":1e2 }".getBytes("UTF-8");

        prepareJson(jr,
                    positive);
        Assertions.assertTrue(BigDecimal.valueOf(1e2d).compareTo(checkDecimalError(jr,
                                                                                   null)) == 0);
        prepareJson(jr,
                    positive);
        Assertions.assertTrue(BigDecimal.valueOf(1e2f).compareTo(checkDecimalError(jr,
                                                                                   null)) == 0);
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(0,
                                BigDecimal.valueOf(1e2).compareTo(checkDecimalError(jr,
                                                                                    null)));
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(100,
                                checkIntError(jr,
                                              null));
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(100L,
                                checkLongError(jr,
                                               null));
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(0,
                                BigDecimal.valueOf(1e2).compareTo((BigDecimal) checkNumberError(jr,
                                                                                                null)));

        byte[] negative = "{\"x\":-1e2 }".getBytes("UTF-8");
        prepareJson(jr,
                    negative);
        Assertions.assertTrue(BigDecimal.valueOf(-1e2d).compareTo(checkDecimalError(jr,
                                                                                    null)) == 0);
        prepareJson(jr,
                    negative);
        Assertions.assertTrue(BigDecimal.valueOf(-1e2f).compareTo(checkDecimalError(jr,
                                                                                    null)) == 0);
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(0,
                                BigDecimal.valueOf(-1e2).compareTo(checkDecimalError(jr,
                                                                                     null)));
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(-100,
                                checkIntError(jr,
                                              null));
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(-100L,
                                checkLongError(jr,
                                               null));
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(0,
                                BigDecimal.valueOf(-1e2).compareTo((BigDecimal) checkNumberError(jr,
                                                                                                 null)));
    }

    @Test
    public void exponentWithDecimalSpaceEndParsing() throws IOException {
        JsonReader<Object> jr = dslJson.newReader(new byte[0]);

        byte[] positive = "{\"x\":1.2e3 }".getBytes("UTF-8");

        prepareJson(jr,
                    positive);
        Assertions.assertTrue(BigDecimal.valueOf(1.2e3d).compareTo(
                checkDecimalError(jr,
                                  null)) == 0);
        prepareJson(jr,
                    positive);
        Assertions.assertTrue(BigDecimal.valueOf(1.2e3f).compareTo(
                checkDecimalError(jr,
                                  null)) == 0);
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(0,
                                BigDecimal.valueOf(1.2e3).compareTo(checkDecimalError(jr,
                                                                                      null)));
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(1200,
                                checkIntError(jr,
                                              null));
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(1200L,
                                checkLongError(jr,
                                               null));
        prepareJson(jr,
                    positive);
        Assertions.assertEquals(0,
                                BigDecimal.valueOf(1.2e3).compareTo((BigDecimal) checkNumberError(jr,
                                                                                                  null)));

        byte[] negative = "{\"x\":-1.2e3 }".getBytes("UTF-8");
        prepareJson(jr,
                    negative);
        Assertions.assertTrue(BigDecimal.valueOf(-1.2e3d).compareTo(
                checkDecimalError(jr,
                                  null)) == 0);
        prepareJson(jr,
                    negative);
        Assertions.assertTrue(BigDecimal.valueOf(-1.2e3f).compareTo(
                checkDecimalError(jr,
                                  null)) == 0);
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(0,
                                BigDecimal.valueOf(-1.2e3).compareTo(checkDecimalError(jr,
                                                                                       null)));
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(-1200,
                                checkIntError(jr,
                                              null));
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(-1200L,
                                checkLongError(jr,
                                               null));
        prepareJson(jr,
                    negative);
        Assertions.assertEquals(0,
                                BigDecimal.valueOf(-1.2e3).compareTo((BigDecimal) checkNumberError(jr,
                                                                                                   null)));
    }

    @Test
    public void bidDecimalRandom() throws IOException {
        final JsonWriter sw = new JsonWriter(40,
                                             null);
        final JsonReader<Object> jr = dslJson.newReader(sw.getByteBuffer());
        final JsonReader<Object> jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                                         new byte[64]);

        final Random rnd = new Random(0);

        for (int i = 0; i < 1000000; i++) {
            sw.reset();

            BigDecimal bd =
                    i % 5 == 0 ?
                    BigDecimal.valueOf(rnd.nextLong(),
                                       rnd.nextInt(500))
                               :
                    BigDecimal.valueOf(rnd.nextLong(),
                                       rnd.nextInt(1000) - 500);


            MyNumberConverter.serialize(bd,
                                        sw);

            jr.process(null,
                       sw.size());
            jr.read();
            final BigDecimal dp1 = MyNumberConverter.deserializeDecimal(jr);
            Assertions.assertTrue(bd.compareTo(dp1) == 0);

            final ByteArrayInputStream isd = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                      0,
                                                                      sw.size());
            jsr.process(isd);
            jsr.read();
            final BigDecimal dp2 = MyNumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(bd,
                                    dp2);

            jr.process(null,
                       sw.size());
            jr.read();
            final BigDecimal fp1 = MyNumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(bd,
                                    fp1);

            final ByteArrayInputStream isf = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                      0,
                                                                      sw.size());
            jsr.process(isf);
            jsr.read();
            final BigDecimal fp2 = MyNumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(bd,
                                    fp2);
        }
    }

    @Test
    public void notSupportInvalidNan() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<Double> values = Arrays.asList(1.2,
                                            Double.NaN,
                                            Double.POSITIVE_INFINITY);
        dslJson.serialize(values,
                          os);
        Assertions.assertEquals("[1.2,\"NaN\",\"Infinity\"]",
                                os.toString("UTF-8"));
        List<Double> resultQuoted = dslJson.deserializeList(Double.class,
                                                            os.toByteArray(),
                                                            os.size());
        Assertions.assertEquals(values,
                                resultQuoted);

    }

    @Test
    public void testBigDecimalDeserializationWithProcessStream() throws IOException {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993,[".getBytes("UTF-8");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final BigDecimal actual = MyNumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessStreamWhenAtBoundary() throws IOException {
        final byte[] input = "0.112233445566778899".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final BigDecimal actual = MyNumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessStreamWhenAtBoundaryAndMore() throws IOException {
        final byte[] input = "0.112233445566778899,1".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final BigDecimal actual = MyNumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessBuffer() throws IOException {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes("UTF-8");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final BigDecimal actual = MyNumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessBufferWhenAtBoundary() throws IOException {
        final byte[] input = "0.112233445566778899".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final BigDecimal actual = MyNumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessBufferWhenAtBoundaryAndMore() throws IOException {
        final byte[] input = "0.112233445566778899,1".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final BigDecimal actual = MyNumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithNewStream() throws IOException {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes("UTF-8");
        final JsonReader<Object> jr = dslJson.newReader(new ByteArrayInputStream(input),
                                                        new byte[20]);
        jr.read(); // init start pointer

        final BigDecimal actual = MyNumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithNewStreamWhenAtBoundary() throws IOException {
        final byte[] input = "0.112233445566778899".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new ByteArrayInputStream(input),
                                                        new byte[20]);
        jr.read(); // init start pointer

        final BigDecimal actual = MyNumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithNewStreamWhenAtBoundaryAndMore() throws IOException {
        final byte[] input = "0.112233445566778899,1".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new ByteArrayInputStream(input),
                                                        new byte[20]);
        jr.read(); // init start pointer

        final BigDecimal actual = MyNumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessStream() throws IOException {
        final Number expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes("UTF-8");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final Number actual = MyNumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessStreamWhenAtBoundary() throws IOException {
        final byte[] input = "0.112233445566778899".getBytes("UTF-8");
        final Number expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final Number actual = MyNumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessStreamWhenAtBoundaryAndMore() throws IOException {
        final byte[] input = "0.112233445566778899,1".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final Number actual = MyNumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessBuffer() throws IOException {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes("UTF-8");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final Number actual = MyNumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessBufferWhenAtBoundary() throws IOException {
        final byte[] input = "0.112233445566778899".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final Number actual = MyNumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessBufferWhenAtBoundaryAndMore() throws IOException {
        final byte[] input = "0.112233445566778899,1".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final Number actual = MyNumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithNewStream() throws IOException {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes("UTF-8");
        final JsonReader<Object> jr = dslJson.newReader(new ByteArrayInputStream(input),
                                                        new byte[20]);
        jr.read(); // init start pointer

        final Number actual = MyNumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithNewStreamWhenAtBoundary() throws IOException {
        final byte[] input = "0.112233445566778899".getBytes("UTF-8");
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new ByteArrayInputStream(input),
                                                        new byte[20]);
        jr.read(); // init start pointer

        final Number actual = MyNumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithNewStreamWhenAtBoundaryAndMore() throws IOException {
        final byte[] input = "0.112233445566778899,1".getBytes("UTF-8");
        final Number expected = new BigDecimal("0.112233445566778899");
        final JsonReader<Object> jr = dslJson.newReader(new ByteArrayInputStream(input),
                                                        new byte[20]);
        jr.read(); // init start pointer

        final Number actual = MyNumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }
}
