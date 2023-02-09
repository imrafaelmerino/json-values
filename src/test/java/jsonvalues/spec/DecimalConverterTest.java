package jsonvalues.spec;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class DecimalConverterTest {
    private static final String VALUES =
            "0e-0, 0e-0, 0e-1, 0e-12, 0e+0, 0e+1, 0e+12, 0E-0, 0E-1, 0E-12, 0E+0, 0E+1, 0E+12, 0.0e-0, 0.0e-1, 0.0e-12," +
                    "0.0e+0, 0.0e+1, 0.0e+12, 0.0E-0, 0.0E-1, 0.0E-12, 0.0E+0, 0.0E+1, 0.0E+12, 0.12e-0, 0.12e-1, 0.12e-12," +
                    "0.12e+0, 0.12e+1, 0.12e+12, 0.12E-0, 0.12E-1, 0.12E-12, 0.12E+0, 0.12E+1, 0.12E+12, 1e-0, 1e-1, 1e-12," +
                    "1e+0, 1e+1, 1e+12, 1E-0, 1E-1, 1E-12, 1E+0, 1E+1, 1E+12, 1.0e-0, 1.0e-1, 1.0e-12, 1.0e+0, 1.0e+1, 1.0e+12," +
                    "1.0E-0, 1.0E-1, 1.0E-12, 1.0E+0, 1.0E+1, 1.0E+12, 1.12e-0, 1.12e-1, 1.12e-12, 1.12e+0, 1.12e+1, 1.12e+12," +
                    "1.12E-0, 1.12E-1, 1.12E-12, 1.12E+0, 1.12E+1, 1.12E+12, 12e-0, 12e-1, 12e-12, 12e+0, 12e+1, 12e+12, 12E-0," +
                    "12E-1, 12E-12, 12E+0, 12E+1, 12E+12, 12.0e-0, 12.0e-1, 12.0e-12, 12.0e+0, 12.0e+1, 12.0e+12, 12.0E-0, 12.0E-1," +
                    "12.0E-12, 12.0E+0, 12.0E+1, 12.0E+12, 12.12e-0, 12.12e-1, 12.12e-12, 12.12e+0, 12.12e+1, 12.12e+12, 12.12E-0," +
                    "12.12E-1, 12.12E-12, 12.12E+0, 12.12E+1, 12.12E+12, -0e-0, -0e-1, -0e-12, -0e+0, -0e+1, -0e+12, -0E-0, -0E-1," +
                    "-0E-12, -0E+0, -0E+1, -0E+12, -0.0e-0, -0.0e-1, -0.0e-12, -0.0e+0, -0.0e+1, -0.0e+12, -0.0E-0, -0.0E-1, -0.0E-12," +
                    "-0.0E+0, -0.0E+1, -0.0E+12, -0.12e-0, -0.12e-1, -0.12e-12, -0.12e+0, -0.12e+1, -0.12e+12, -0.12E-0, -0.12E-1," +
                    "-0.12E-12, -0.12E+0, -0.12E+1, -0.12E+12, -1e-0, -1e-1, -1e-12, -1e+0, -1e+1, -1e+12, -1E-0, -1E-1, -1E-12, -1E+0," +
                    "-1E+1, -1E+12, -1.0e-0, -1.0e-1, -1.0e-12, -1.0e+0, -1.0e+1, -1.0e+12, -1.0E-0, -1.0E-1, -1.0E-12, -1.0E+0, -1.0E+1," +
                    "-1.0E+12, -1.12e-0, -1.12e-1, -1.12e-12, -1.12e+0, -1.12e+1, -1.12e+12, -1.12E-0, -1.12E-1, -1.12E-12, -1.12E+0," +
                    "-1.12E+1, -1.12E+12, -12e-0, -12e-1, -12e-12, -12e+0, -12e+1, -12e+12, -12E-0, -12E-1, -12E-12, -12E+0, -12E+1," +
                    "-12E+12, -12.0e-0, -12.0e-1, -12.0e-12, -12.0e+0, -12.0e+1, -12.0e+12, -12.0E-0, -12.0E-1, -12.0E-12, -12.0E+0," +
                    "-12.0E+1, -12.0E+12, -12.12e-0, -12.12e-1, -12.12e-12, -12.12e+0, -12.12e+1, -12.12e+12, -12.12E-0, -12.12E-1," +
                    "-12.12E-12, -12.12E+0, -12.12E+1, -12.12E+12 ";

    private final JsonIO dslJson = JsonIO.INSTANCE;

    @Test
    public void testSerialization()  {
        // setup
        final String[] values = VALUES.split(", *");
        final int count = values.length;

        final byte[] buf = new byte[1024];
        final JsWriter jw = new JsWriter(buf);

        for (int i = 0; i < count - 1; i++) {
            // setup
            final BigDecimal direct = new BigDecimal(values[i]);
            jw.reset();

            // serialization
            NumberConverter.serialize(direct,
                                      jw);

            // check
            final BigDecimal current = new BigDecimal(jw.toString());
            if (direct.compareTo(current) != 0) {
                Assertions.fail("Written BigDecimal was not equal to the test value; " + direct + " != " + current);
            }
        }
        for (int i = 0; i < count - 1; i++) {
            // setup
            final BigDecimal direct = BigDecimal.valueOf(Double.parseDouble(values[i]));
            jw.reset();

            // serialization
            NumberConverter.serialize(direct,
                                      jw);

            // check
            final BigDecimal current = BigDecimal.valueOf(Double.parseDouble(jw.toString()));
            if (!direct.equals(current)) {
                Assertions.fail("Written double was not equal to the test value; " + direct + " != " + current);
            }
        }
        for (int i = 0; i < count - 1; i++) {
            // setup
            final BigDecimal direct = BigDecimal.valueOf(Float.parseFloat(values[i]));
            jw.reset();

            // serialization
            NumberConverter.serialize(direct,
                                      jw);

            // check
            final BigDecimal current = BigDecimal.valueOf(Float.parseFloat(jw.toString()));
            if (!direct.equals(current)) {
                Assertions.fail("Written float was not equal to the test value; " + direct + " != " + current);
            }
        }
    }

    @Test
    public void testDeserialization() throws IOException {
        // setup
        final String[] values = VALUES.split(", *");
        final int count = values.length;

        final byte[] buf = VALUES.getBytes(StandardCharsets.ISO_8859_1);
        JsReader jr = dslJson.newReader(buf);
        JsReader jsr = dslJson.newReader(new ByteArrayInputStream(buf),
                                         new byte[64]);

        // first digit in values
        Assertions.assertEquals('0',
                                jr.getNextToken());
        Assertions.assertEquals('0',
                                jsr.getNextToken());

        for (int i = 0; i < count - 1; i++) {
            if (i > 0) {
                jr.getNextToken();//','
                jsr.getNextToken();//','
                jr.getNextToken();//' '
                jsr.getNextToken();//' '
            }

            // setup
            final BigDecimal direct = new BigDecimal(values[i]);

            // deserialiaztion
            final BigDecimal current1 = NumberConverter.deserializeDecimal(jr);
            final BigDecimal current2 = NumberConverter.deserializeDecimal(jsr);

            //check
            if (direct.compareTo(current1) != 0) {
                Assertions.fail("Parsed BigDecimal was not equal to the test value; expected " + direct + ", but actual was " + current1 + ". Used value: " + values[i]);
            } else if (direct.compareTo(current2) != 0) {
                Assertions.fail("Parsed BigDecimal was not equal to the test value; expected " + direct + ", but actual was " + current2 + ". Used value: " + values[i]);
            }
        }

        jr = dslJson.newReader(buf);
        jsr = dslJson.newReader(new ByteArrayInputStream(buf),
                                new byte[64]);

        // first digit in values
        Assertions.assertEquals('0',
                                jr.getNextToken());
        Assertions.assertEquals('0',
                                jsr.getNextToken());

        for (int i = 0; i < count - 1; i++) {
            if (i > 0) {
                jr.getNextToken();//','
                jsr.getNextToken();//','
                jr.getNextToken();//' '
                jsr.getNextToken();//' '
            }

            // setup
            final BigDecimal direct = BigDecimal.valueOf(Double.parseDouble(values[i]));

            // deserialiaztion
            final BigDecimal current1 = NumberConverter.deserializeDecimal(jr);
            final BigDecimal current2 = NumberConverter.deserializeDecimal(jsr);

            //check
            if (direct.compareTo(current1) != 0) {
                Assertions.fail("Parsed double was not equal to the test value; expected " + direct + ", but actual was " + current1 + ". Used value: " + values[i]);
            } else if (direct.compareTo(current2) != 0) {
                Assertions.fail("Parsed double was not equal to the test value; expected " + direct + ", but actual was " + current2 + ". Used value: " + values[i]);
            }
        }

        jr = dslJson.newReader(buf);
        jsr = dslJson.newReader(new ByteArrayInputStream(buf),
                                new byte[64]);

        // first digit in values
        Assertions.assertEquals('0',
                                jr.getNextToken());
        Assertions.assertEquals('0',
                                jsr.getNextToken());


    }

    @Test
    public void testPowersOf10() throws IOException {
        for (int i = -500; i < 500; i++) {
            final String sciForm = "1E" + i;
            final BigDecimal check = new BigDecimal(sciForm);

            // space to prevent end of stream gotcha
            final String plainForm = check.toPlainString();
            final byte[] body = plainForm.getBytes(StandardCharsets.ISO_8859_1);

            final JsReader jr = dslJson.newReader(body);
            jr.getNextToken();
            final BigDecimal parsed1 = NumberConverter.deserializeDecimal(jr);

            final JsReader jsr = dslJson.newReader(new ByteArrayInputStream(body),
                                                   new byte[64]);
            jsr.getNextToken();
            final BigDecimal parsed2 = NumberConverter.deserializeDecimal(jsr);

            if (parsed1.compareTo(check) != 0) {
                Assertions.fail("Mismatch in decimals; expected " + check + ", but actual was " + parsed1);
            } else if (parsed2.compareTo(check) != 0) {
                Assertions.fail("Mismatch in decimals; expected " + check + ", but actual was " + parsed2);
            }
        }
    }




}
