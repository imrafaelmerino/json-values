package jsonvalues.spec;


import jsonvalues.JsParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class NumberConverterTest {

    private final JsIO dslJson = JsIO.INSTANCE;

    @Test
    public void rangeCheckInt() throws JsParserException {
        // setup
        final JsWriter sw = new JsWriter(40);
        final JsReader jr = dslJson.newReader(sw.getByteBuffer());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                               new byte[64]);

        final int from = -10000000;
        final int to = 10000000;

        for (int value = from; value <= to; value += 33) {
            sw.reset();

            // serialization
            NumberConverter.serialize(value,
                                      sw);

            jr.process(null,
                       sw.size());
            jr.read();
            final int valueParsed1 = NumberConverter.deserializeInt(jr);
            Assertions.assertEquals(value,
                                    valueParsed1);

            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final int valueParsed2 = NumberConverter.deserializeInt(jsr);
            Assertions.assertEquals(value,
                                    valueParsed2);
        }
    }

    @Test
    public void rangeCheckLong() throws JsParserException {
        // setup
        final JsWriter sw = new JsWriter(40);
        final JsReader jr = dslJson.newReader(sw.getByteBuffer());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                               new byte[64]);

        final long from = -10000000000L;
        final long to = 10000000000L;

        for (long value = from; value <= to; value += 33333) {
            sw.reset();

            // serialization
            NumberConverter.serialize(value,
                                      sw);

            jr.process(null,
                       sw.size());
            jr.read();
            final long valueParsed1 = NumberConverter.deserializeLong(jr);
            Assertions.assertEquals(value,
                                    valueParsed1);

            jr.process(null,
                       sw.size());
            jr.read();
            final Number numberParsed1 = NumberConverter.deserializeNumber(jr);
            Assertions.assertEquals(value,
                                    numberParsed1);

            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final long valueParsed2 = NumberConverter.deserializeLong(jsr);
            Assertions.assertEquals(value,
                                    valueParsed2);

            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final Number numberParsed2 = NumberConverter.deserializeNumber(jsr);
            Assertions.assertEquals(value,
                                    numberParsed2);
        }
    }

    @Test
    public void rangeCheckDecimal() throws JsParserException {
        // setup
        final JsWriter sw = new JsWriter(40);
        final JsReader jr = dslJson.newReader(sw.getByteBuffer());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                               new byte[64]);

        final int from = -100000000;
        final int to = 100000000;
        final double[] dividers = {1, 10, 0.1, 100, 0.01, 1000, 0.001};
        int x = 0;

        for (int value = from; value <= to; value += 333) {
            sw.reset();

            // serialization
            BigDecimal bd = BigDecimal.valueOf(value / dividers[x++ % dividers.length]);
            NumberConverter.serialize(bd,
                                      sw);

            jr.process(null,
                       sw.size());
            jr.read();
            final BigDecimal valueParsed1 = NumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(bd,
                                    valueParsed1);
            jr.process(null,
                       sw.size());
            jr.read();
            final Number numberParsed1 = NumberConverter.deserializeNumber(jr);
            Assertions.assertEquals(bd,
                                    BigDecimal.valueOf(numberParsed1.doubleValue()));

            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final BigDecimal valueParsed2 = NumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(bd,
                                    valueParsed2);
            jsr.process(new ByteArrayInputStream(sw.getByteBuffer(),
                                                 0,
                                                 sw.size()));
            jsr.read();
            final Number numberParsed2 = NumberConverter.deserializeNumber(jsr);
            Assertions.assertEquals(bd,
                                    BigDecimal.valueOf(numberParsed2.doubleValue()));
        }
    }


    @Test
    public void testSerialization() {
        // setup
        final JsWriter sw = new JsWriter(40);

        final int from = -1000000;
        final int to = 1000000;

        for (long value = from; value <= to; value++) {

            // init
            sw.reset();

            // serialization
            NumberConverter.serialize(value,
                                      sw);

            // check
            final String valueString = sw.toString();
            final int valueParsed = Integer.parseInt(valueString);
            Assertions.assertEquals(value,
                                    valueParsed);
        }
    }


    @Test
    public void testPowersOf10() throws JsParserException {
        String sciForm = "1";

        final int maxLen = Long.toString(Long.MAX_VALUE).length();
        for (int i = 0; i < maxLen; i++) {
            // space to prevent end of stream gotcha
            final byte[] body = (sciForm + " ").getBytes(StandardCharsets.ISO_8859_1);

            final JsReader jr = dslJson.newReader(body);
            jr.readNextToken();
            final long parsed1 = NumberConverter.deserializeLong(jr);
            jr.process(new byte[64],
                       64);
            jr.process(new ByteArrayInputStream(body));
            jr.readNextToken();
            final long parsed2 = NumberConverter.deserializeLong(jr);

            final long check = Long.parseLong(sciForm);
            Assertions.assertEquals(check,
                                    parsed1);
            Assertions.assertEquals(check,
                                    parsed2);

            sciForm += '0';
        }
    }

    
    @Test
    public void testGenericNumberLongBoundaries() {
        final Long maxIntAsLong = (long) Integer.MAX_VALUE;
        final Long minIntAsLong = (long) Integer.MIN_VALUE;
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

        var result = new JsObjSpecParser(JsSpecs.obj()).parse(input);
        Assertions.assertEquals(maxIntAsLong,
                                result.getLong("maxIntAsLong"));
        Assertions.assertEquals(minIntAsLong,
                                result.getLong("minIntAsLong"));
        Assertions.assertEquals(maxIntWithDecimalAsBigDecimal,
                                result.getBigDec("maxIntWithDecimalAsBigDecimal"));
        Assertions.assertEquals(minIntWithDecimalAsBigDecimal,
                                result.getBigDec("minIntWithDecimalAsBigDecimal"));
        Assertions.assertEquals(positive18DigitLong,
                                result.getLong("positive18DigitLong"));
        Assertions.assertEquals(negative18DigitLong,
                                result.getLong("negative18DigitLong"));
        Assertions.assertEquals(positive18DigitAndOneDecimal,
                                result.getBigDec("positive18DigitAndOneDecimal"));
        Assertions.assertEquals(negative18DigitAndOneDecimal,
                                result.getBigDec("negative18DigitAndOneDecimal"));
        Assertions.assertEquals(maxLong,
                                result.getLong("maxLong"));
        Assertions.assertEquals(minLong,
                                result.getLong("minLong"));
        Assertions.assertEquals(maxLongPlusOneAsBigDecimal,
                                result.getBigDec("maxLongPlusOneAsBigDecimal"));
        Assertions.assertEquals(minLongMinusOneAsBigDecimal,
                                result.getBigDec("minLongMinusOneAsBigDecimal"));
    }

    @Test
    public void shortWhitespaceGuard() throws JsParserException {
        String input = "1234  ";
        final JsReader jr = dslJson.newReader(input.getBytes());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(input.getBytes()),
                                               new byte[64]);
        jr.readNextToken();
        Number number = NumberConverter.deserializeNumber(jr);
        Assertions.assertTrue(number instanceof Long);
        jsr.readNextToken();
        number = NumberConverter.deserializeNumber(jsr);
        Assertions.assertTrue(number instanceof Long);
    }

    @Test
    public void longWhitespaceGuard() throws JsParserException {
        String input = "1234        \t\n\r               ";
        final JsReader reader = dslJson.newReader(input.getBytes());
        reader.readNextToken();
        Number number = NumberConverter.deserializeNumber(reader);
        Assertions.assertTrue(number instanceof Long);
    }

    @Test
    public void overflowDetection() throws JsParserException {
        String input = "1234567890123456        \t\n\r               ";
        JsReader reader = dslJson.newReader(input.getBytes());
        reader.readNextToken();
        try {
            NumberConverter.deserializeInt(reader);
            Assertions.fail();
        } catch (JsParserException e) {
            Assertions.assertTrue(e.getMessage().contains("Integer overflow"));
        }
        input = "-1234567890123456        \t\n\r               ";
        reader = dslJson.newReader(input.getBytes());
        reader.readNextToken();
        try {
            NumberConverter.deserializeInt(reader);
            Assertions.fail();
        } catch (JsParserException e) {
            Assertions.assertTrue(e.getMessage().contains("Integer overflow"));
        }
    }

    @Test
    public void doubleRandom() throws JsParserException {
        final JsWriter sw = new JsWriter(40);
        final JsReader jr = dslJson.newReader(sw.getByteBuffer());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                               new byte[64]);

        final Random rnd = new Random(0);

        for (int i = 0; i < 1000000; i++) {
            sw.reset();

            // serialization
            BigDecimal d = BigDecimal.valueOf(rnd.nextDouble());
            NumberConverter.serialize(d,
                                      sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = NumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(d,
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = NumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(d,
                                    valueParsed2);
        }
    }

    @Test
    public void doubleIntRandom() throws JsParserException {
        final JsWriter sw = new JsWriter(40);
        final JsReader jr = dslJson.newReader(sw.getByteBuffer());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                               new byte[64]);

        final Random rnd = new Random(0);

        for (int i = 0; i < 1000000; i++) {
            sw.reset();

            // serialization
            BigDecimal d = BigDecimal.valueOf((rnd.nextDouble() * rnd.nextInt()));
            NumberConverter.serialize(d,
                                      sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = NumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(d,
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = NumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(d,
                                    valueParsed2);
        }
    }

    @Test
    public void floatRandom() throws JsParserException {
        // setup
        final JsWriter sw = new JsWriter(40);
        final JsReader jr = dslJson.newReader(sw.getByteBuffer());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                               new byte[64]);

        final Random rnd = new Random(0);

        for (int i = 0; i < 1000000; i++) {
            sw.reset();

            // serialization
            BigDecimal f = BigDecimal.valueOf(rnd.nextFloat());
            NumberConverter.serialize(f,
                                      sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = NumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(f,
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = NumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(f,
                                    valueParsed2);
        }
    }

    @Test
    public void floatIntRandom() throws JsParserException {
        // setup
        final JsWriter sw = new JsWriter(40);
        final JsReader jr = dslJson.newReader(sw.getByteBuffer());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
                                               new byte[64]);

        final Random rnd = new Random(0);

        for (int i = 0; i < 1000000; i++) {
            sw.reset();

            // serialization
            BigDecimal d = BigDecimal.valueOf((float) rnd.nextDouble() * rnd.nextInt());
            NumberConverter.serialize(d,
                                      sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = NumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(d,
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = NumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(d,
                                    valueParsed2);
        }
    }

    private void prepareJson(JsReader reader,
                             byte[] input) throws JsParserException {
        reader.process(input,
                       input.length);
        reader.read();
        reader.read();
        reader.read();
    }


    private BigDecimal checkDecimalError(JsReader reader,
                                         String error) {
        BigDecimal res = null;
        try {
            res = NumberConverter.deserializeDecimal(reader);
            if (error != null) Assertions.fail("Expecting " + error);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assertions.assertTrue(ex.getMessage().contains(error));
        }
        return res;
    }

    private int checkIntError(JsReader reader,
                              String error) {
        int res = 0;
        try {
            res = NumberConverter.deserializeInt(reader);
            if (error != null) Assertions.fail("Expecting " + error);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assertions.assertTrue(ex.getMessage().contains(error));
        }
        return res;
    }

    private long checkLongError(JsReader reader,
                                String error) {
        long res = 0;
        try {
            res = NumberConverter.deserializeLong(reader);
            if (error != null) Assertions.fail("Expecting " + error);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assertions.assertTrue(ex.getMessage().contains(error));
        }
        return res;
    }

    private Number checkNumberError(JsReader reader,
                                    String error) {
        Number res = null;
        try {
            res = NumberConverter.deserializeNumber(reader);
            if (error != null) Assertions.fail("Expecting " + error);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Assertions.assertTrue(ex.getMessage().contains(error));
        }
        return res;
    }

    @Test
    public void emptyParsing() throws JsParserException {
        final JsReader jr = dslJson.newReader(new byte[0]);

        byte[] empty = "{\"x\":}".getBytes(StandardCharsets.UTF_8);
        byte[] space = "{\"x\": }".getBytes(StandardCharsets.UTF_8);
        byte[] plus = "{\"x\":+}".getBytes(StandardCharsets.UTF_8);
        byte[] minus = "{\"x\":-}".getBytes(StandardCharsets.UTF_8);
        byte[] e = "{\"x\":e}".getBytes(StandardCharsets.UTF_8);
        byte[] plusSpace = "{\"x\":+ }".getBytes(StandardCharsets.UTF_8);
        byte[] minusSpace = "{\"x\":- }".getBytes(StandardCharsets.UTF_8);
        byte[] eSpace = "{\"x\":E }".getBytes(StandardCharsets.UTF_8);
        byte[] dot = "{\"x\":.}".getBytes(StandardCharsets.UTF_8);
        byte[] doubleMinus = "{\"x\":--0}".getBytes(StandardCharsets.UTF_8);
        byte[] doubleMinusSpace = "{\"x\":--0}".getBytes(StandardCharsets.UTF_8);
        byte[] doubleZero = "{\"x\":00}".getBytes(StandardCharsets.UTF_8);
        byte[] doubleNegativeZero = "{\"x\":-00}".getBytes(StandardCharsets.UTF_8);
        byte[] doubleZeroAndSpace = "{\"x\":00 }".getBytes(StandardCharsets.UTF_8);
        byte[] leadingZero = "{\"x\":01}".getBytes(StandardCharsets.UTF_8);
        byte[] leadingNegativeZero = "{\"x\":-01}".getBytes(StandardCharsets.UTF_8);
        byte[] leadingZeroAndSpace = "{\"x\":01 }".getBytes(StandardCharsets.UTF_8);
        byte[] quotedLeadingZero = "{\"x\":\"01\"}".getBytes(StandardCharsets.UTF_8);
        byte[] quotedLeadingNegativeZero = "{\"x\":\"-01\"}".getBytes(StandardCharsets.UTF_8);
        byte[] quotedLeadingZeroAndSpace = "{\"x\":\"01\" }".getBytes(StandardCharsets.UTF_8);

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
    public void wrongSpaceParsing() throws JsParserException {
        final JsReader jr = dslJson.newReader(new byte[0]);

        byte[] doubleZero = "{\"x\":0 0}".getBytes(StandardCharsets.UTF_8);
        byte[] doubleDot1 = "{\"x\":0.0.}".getBytes(StandardCharsets.UTF_8);
        byte[] doubleDot2 = "{\"x\":0..0}".getBytes(StandardCharsets.UTF_8);
        byte[] dotNoNumber1 = "{\"x\":.0}".getBytes(StandardCharsets.UTF_8);
        byte[] dotNoNumber2 = "{\"x\":0.}".getBytes(StandardCharsets.UTF_8);

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
    public void specialFloats() throws JsParserException {
        final JsReader jr = dslJson.newReader(new byte[0]);
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
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

            byte[] input = d.getBytes(StandardCharsets.UTF_8);
            jr.process(input,
                       input.length);
            jr.read();

            final BigDecimal valueParsed1 = NumberConverter.deserializeDecimal(jr);

            Assertions.assertEquals(0, f.compareTo(valueParsed1));

            final ByteArrayInputStream is = new ByteArrayInputStream(input,
                                                                     0,
                                                                     input.length);
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = NumberConverter.deserializeDecimal(jsr);
            System.out.println(f);
            System.out.println(valueParsed2);
            Assertions.assertEquals(0, f.compareTo(valueParsed2));
        }
    }

    @Test
    public void doubleRoundingError() throws JsParserException {
        final JsIO dslJson = new JsIO(new Settings().doublePrecision(JsReader.DoublePrecision.DEFAULT));
        final JsWriter sw = new JsWriter(40);
        final JsReader jr = dslJson.newReader(sw.getByteBuffer());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
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

            NumberConverter.serialize(BigDecimal.valueOf(d),
                                      sw);

            jr.process(null,
                       sw.size());
            jr.read();

            final BigDecimal valueParsed1 = NumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(BigDecimal.valueOf(d),
                                    valueParsed1);

            final ByteArrayInputStream is = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                     0,
                                                                     sw.size());
            jsr.process(is);
            jsr.read();

            final BigDecimal valueParsed2 = NumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(BigDecimal.valueOf(d),
                                    valueParsed2);
        }
    }




    @Test
    public void bidDecimalRandom()  {
        final JsWriter sw = new JsWriter(40);
        final JsReader jr = dslJson.newReader(sw.getByteBuffer());
        final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(new byte[0]),
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


            NumberConverter.serialize(bd,
                                      sw);

            jr.process(null,
                       sw.size());
            jr.read();
            final BigDecimal dp1 = NumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(0, bd.compareTo(dp1));

            final ByteArrayInputStream isd = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                      0,
                                                                      sw.size());
            jsr.process(isd);
            jsr.read();
            final BigDecimal dp2 = NumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(bd,
                                    dp2);

            jr.process(null,
                       sw.size());
            jr.read();
            final BigDecimal fp1 = NumberConverter.deserializeDecimal(jr);
            Assertions.assertEquals(bd,
                                    fp1);

            final ByteArrayInputStream isf = new ByteArrayInputStream(sw.getByteBuffer(),
                                                                      0,
                                                                      sw.size());
            jsr.process(isf);
            jsr.read();
            final BigDecimal fp2 = NumberConverter.deserializeDecimal(jsr);
            Assertions.assertEquals(bd,
                                    fp2);
        }
    }



    @Test
    public void testBigDecimalDeserializationWithProcessStream()  {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993,[".getBytes(StandardCharsets.UTF_8);
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final BigDecimal actual = NumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessStreamWhenAtBoundary()  {
        final byte[] input = "0.112233445566778899".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final BigDecimal actual = NumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessStreamWhenAtBoundaryAndMore()  {
        final byte[] input = "0.112233445566778899,1".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final BigDecimal actual = NumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessBuffer()  {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes(StandardCharsets.UTF_8);
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final BigDecimal actual = NumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessBufferWhenAtBoundary()  {
        final byte[] input = "0.112233445566778899".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final BigDecimal actual = NumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithProcessBufferWhenAtBoundaryAndMore()  {
        final byte[] input = "0.112233445566778899,1".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final BigDecimal actual = NumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithNewStream()  {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes(StandardCharsets.UTF_8);
        final JsReader jr = dslJson.newReader(new ByteArrayInputStream(input),
                                              new byte[20]);
        jr.read(); // init start pointer

        final BigDecimal actual = NumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithNewStreamWhenAtBoundary()  {
        final byte[] input = "0.112233445566778899".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new ByteArrayInputStream(input),
                                              new byte[20]);
        jr.read(); // init start pointer

        final BigDecimal actual = NumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testBigDecimalDeserializationWithNewStreamWhenAtBoundaryAndMore()  {
        final byte[] input = "0.112233445566778899,1".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new ByteArrayInputStream(input),
                                              new byte[20]);
        jr.read(); // init start pointer

        final BigDecimal actual = NumberConverter.deserializeDecimal(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessStream()  {
        final Number expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes(StandardCharsets.UTF_8);
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final Number actual = NumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessStreamWhenAtBoundary()  {
        final byte[] input = "0.112233445566778899".getBytes(StandardCharsets.UTF_8);
        final Number expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final Number actual = NumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessStreamWhenAtBoundaryAndMore()  {
        final byte[] input = "0.112233445566778899,1".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(new ByteArrayInputStream(input));
        jr.read(); // init start pointer

        final Number actual = NumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessBuffer()  {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes(StandardCharsets.UTF_8);
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final Number actual = NumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessBufferWhenAtBoundary()  {
        final byte[] input = "0.112233445566778899".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final Number actual = NumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithProcessBufferWhenAtBoundaryAndMore()  {
        final byte[] input = "0.112233445566778899,1".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new byte[20]);
        jr.process(input,
                   input.length);
        jr.read(); // init start pointer

        final Number actual = NumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithNewStream()  {
        final BigDecimal expected = new BigDecimal("0.112233445566778899001122334455667788993");
        final byte[] input = "0.112233445566778899001122334455667788993".getBytes(StandardCharsets.UTF_8);
        final JsReader jr = dslJson.newReader(new ByteArrayInputStream(input),
                                              new byte[20]);
        jr.read(); // init start pointer

        final Number actual = NumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithNewStreamWhenAtBoundary()  {
        final byte[] input = "0.112233445566778899".getBytes(StandardCharsets.UTF_8);
        final BigDecimal expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new ByteArrayInputStream(input),
                                              new byte[20]);
        jr.read(); // init start pointer

        final Number actual = NumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }

    @Test
    public void testNumberDeserializationWithNewStreamWhenAtBoundaryAndMore()  {
        final byte[] input = "0.112233445566778899,1".getBytes(StandardCharsets.UTF_8);
        final Number expected = new BigDecimal("0.112233445566778899");
        final JsReader jr = dslJson.newReader(new ByteArrayInputStream(input),
                                              new byte[20]);
        jr.read(); // init start pointer

        final Number actual = NumberConverter.deserializeNumber(jr);

        Assertions.assertEquals(expected,
                                actual);
    }
}
