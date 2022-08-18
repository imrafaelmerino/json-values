package com.dslplatform.json;

import jsonvalues.JsBigDec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;


public class DslNumberConverter {

    /**
     * this is the reason I implemented MyNumberConverter . I consider this a bug from dsl-json
     * strings are not numbers
     *
     * @throws IOException
     */
    @Test
    public void testNumber() throws IOException {
        DslJson<Object> dslJson = new DslJson();
        Supplier<JsonReader> reader = () -> {
            try {
                JsonReader r = dslJson.newReader("\"1\"".getBytes(StandardCharsets.UTF_8));
                r.getNextToken();
                return r;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Assertions.assertEquals(1,
                                NumberConverter.deserializeInt(reader.get()));

        //my implementation returns an error
        Assertions.assertThrows(JsParserException.class,
                                () -> MyNumberConverter.deserializeInt(reader.get()));

    }

    @Test
    public void deserializeDecimal() throws IOException {

        DslJson<Object> dslJson = new DslJson();
        Supplier<JsonReader> reader = () -> {
            try {
                JsonReader r = dslJson.newReader("-3.6914651842717967331576316562991741501E-197".getBytes(StandardCharsets.UTF_8));
                r.getNextToken();
                return r;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };


        Assertions.assertEquals(JsBigDec.of(new BigDecimal("-3.6914651842717967331576316562991741501E-197")),
                                JsBigDec.of(MyNumberConverter.deserializeDecimal(reader.get())));




    }

    @Test
    public void deserializeDecimal1() throws IOException {

        DslJson<Object> dslJson = new DslJson();
        Supplier<JsonReader> reader = () -> {
            try {
                JsonReader r = dslJson.newReader("1.705166916240390773522933255090892244333869968630006064E+114".getBytes(StandardCharsets.UTF_8));
                r.getNextToken();
                return r;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Assertions.assertEquals(JsBigDec.of(new BigDecimal("1.705166916240390773522933255090892244333869968630006064E+114")),
                                JsBigDec.of(MyNumberConverter.deserializeDecimal(reader.get())));
    }


    @Test
    public void a() throws IOException {

        DslJson<Object> dslJson = new DslJson();
        Supplier<JsonReader> reader = () -> {
            try {
                JsonReader r = dslJson.newReader("2669345564994622469.0".getBytes(StandardCharsets.UTF_8));
                r.getNextToken();
                return r;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        double d = NumberConverter.deserializeDouble(reader.get());

        Assertions.assertEquals(2669345564994622469L,
                                ((long) d));




    }

}
