package jsonvalues.benchmark;

import jsonvalues.JsParserException;
import jsonvalues.spec.JsObjSpec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static jsonvalues.spec.JsSpecs.*;

public class Fun {
    public static final String PERSON_JSON;

    public static final String PERSON_JSON_SCHEMA;

    public static final JsObjSpec PERSON_SPEC;

    static {
        PERSON_SPEC = JsObjSpec.of("firstName",
                                       str(length(1,
                                                  255)),
                                       "lastName",
                                       str(length(1,
                                                  255)),
                                       "age",
                                       integer(interval(0,
                                                        110)),
                                       "latitude",
                                       decimal(interval(new BigDecimal(-90),
                                                        new BigDecimal(90)
                                                       )
                                              ),
                                       "longitude",
                                       decimal(interval(new BigDecimal(-180),
                                                        new BigDecimal(180)
                                                       )
                                              ),
                                       "fruits",
                                       arrayOfStr(1,100),
                                       "numbers",
                                       arrayOfInt(1,100),
                                       "vegetables",
                                       arrayOfObjSpec(JsObjSpec.of("veggieName",
                                                                str(length(1,
                                                                           255)),
                                                                "veggieLike",
                                                                bool()
                                                               )
                                              )
                                      ).withOptKeys("vegetables");
        PERSON_JSON_SCHEMA = fileContent("personSchema.json");
        PERSON_JSON = fileContent("person.json");


    }

    private static String fileContent(String name) {
        final InputStream stream = Fun.class.getClassLoader()
                                            .getResourceAsStream(name);
        try {
            return fromStream(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static String fromStream(InputStream in) throws IOException {
        BufferedReader reader  = new BufferedReader(new InputStreamReader(in));
        StringBuilder  out     = new StringBuilder();
        String         newLine = System.getProperty("line.separator");
        String         line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }


    private static IntPredicate interval(int min,
                                         int max) {
        return i -> i >= min && i <= max;
    }

    private static Predicate<BigDecimal> interval(BigDecimal min,
                                                  BigDecimal max
                                                 ) {
        return v -> v.doubleValue() <= max.doubleValue() && v.doubleValue() >= min.doubleValue();
    }

    private static Predicate<String> length(int min,
                                            int max) {
        return s -> s.length() >= min && s.length() <= max;

    }
}
