package jsonvalues.spec;

import jsonvalues.JsBigDec;
import jsonvalues.JsParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;


public class DslNumberConverterTest {

    /**
     * this is the reason I implemented MyNumberConverter . I consider this a bug from dsl-json
     * strings are not numbers
     *
     */
    @Test
    public void testNumber()  {
        JsonIO dslJson = new JsonIO();
        Supplier<JsReader> reader = () -> {
            try {
                JsReader r = dslJson.newReader("\"1\"".getBytes(StandardCharsets.UTF_8));
                r.getNextToken();
                return r;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        //my implementation returns an error
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeInt(reader.get()));

    }

    @Test
    public void deserializeDecimal() throws IOException {

        JsonIO dslJson = new JsonIO();
        Supplier<JsReader> reader = () -> {
            try {
                JsReader r = dslJson.newReader("-3.6914651842717967331576316562991741501E-197".getBytes(StandardCharsets.UTF_8));
                r.getNextToken();
                return r;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };


        Assertions.assertEquals(JsBigDec.of(new BigDecimal("-3.6914651842717967331576316562991741501E-197")),
                                JsBigDec.of(NumberConverter.deserializeDecimal(reader.get())));




    }

    @Test
    public void deserializeDecimal1() throws IOException {

        JsonIO dslJson = new JsonIO();
        Supplier<JsReader> reader = () -> {
            try {
                JsReader r = dslJson.newReader("1.705166916240390773522933255090892244333869968630006064E+114".getBytes(StandardCharsets.UTF_8));
                r.getNextToken();
                return r;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Assertions.assertEquals(JsBigDec.of(new BigDecimal("1.705166916240390773522933255090892244333869968630006064E+114")),
                                JsBigDec.of(NumberConverter.deserializeDecimal(reader.get())));
    }




}
