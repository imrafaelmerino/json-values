package jsonvalues.benchmark.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonvalues.JsObj;
import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static jsonvalues.spec.JsSpecs.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ParseStringWithSpec {


    private final static String objectUTStr = "{" + "  \"a\": \"a\"," + "  \"b\": 2," + "  \"c\": true," + "  \"d\": {" + "    \"e\": \"b\"," + "    \"f\": false," + "    \"integers\": [" + "1," + "2," + "3," + "4," + "5," + "6," + "7," + "8," + "9," + "10," + "11," + "12," + "13," + "14," + "15," + "16" + "    ]," + "    \"strings\": [" + "\"1\"," + "\"2\"," + "\"3\"," + "\"4\"," + "\"5\"," + "\"6\"," + "\"7\"," + "" + "\"8\"," + "\"9\"," + "\"10\"," + "\"11\"," + "\"12\"," + "\"13\"," + "\"14\"," + "\"15\"," + "\"16\"" + "    ]," + "    \"objects\": [" + "{" + "  \"a\": \"hi\"," + "  \"b\": 10," + "  \"c\": true," + "  \"d\": [" + "    \"a\"," + "    \"b\"," + "    \"c\"," + "    \"d\"," + "    \"e\"," + "    \"f\"," + "    \"g\"" + "  ]," + "  \"e\": 1.10" + "}," + "{" + "  \"a\": \"hi\"," + "  \"b\": 10," + "  \"c\": true," + "  \"d\": [" + "    \"a\"," + "    \"b\"," + "    \"c\"," + "    \"d\"," + "    \"e\"," + "    \"f\"," + "    \"g\"" + "  ]," + "  \"e\": 1.10" + "}" + "    ]" + "  }" + "}  ";

    private final static byte[] objectUT = objectUTStr.getBytes();

    private final static JsObjSpec spec = JsObjSpec.strict("a",
                                                           str,
                                                           "b",
                                                           integer,
                                                           "c",
                                                           bool,
                                                           "d",
                                                           JsObjSpec.strict("e",
                                                                            str,
                                                                            "f",
                                                                            bool,
                                                                            "integers",
                                                                            arrayOfInt,
                                                                            "strings",
                                                                            arrayOfStr,
                                                                            "objects",
                                                                            arrayOf(JsObjSpec.strict("a",
                                                                                                     str,
                                                                                                     "b",
                                                                                                     integer,
                                                                                                     "c",
                                                                                                     bool,
                                                                                                     "d",
                                                                                                     arrayOfStr,
                                                                                                     "e",
                                                                                                     decimal
                                                                                                    )
                                                                                   )
                                                                           )
                                                          );

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final static JsObjParser jsonParser = new JsObjParser(spec);

    @Benchmark
    public void json_values_with_spec(Blackhole bh) {
        bh.consume(jsonParser.parse(objectUT));
    }

    @Benchmark
    public void jackson(Blackhole bh) throws IOException {
        bh.consume(objectMapper.readValue(objectUT,
                                          Map.class
                                         )
                  );
    }

    @Benchmark
    public void json_values(Blackhole bh) {
        bh.consume(JsObj.parse(objectUTStr));
    }
}
