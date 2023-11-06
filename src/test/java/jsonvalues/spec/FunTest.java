package jsonvalues.spec;


import jsonvalues.JsPath;
import jsonvalues.JsValue;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Set;


class FunTest {

    private FunTest() {
    }

    public static void assertErrorIs(final List<SpecError> error,
                                     final ERROR_CODE code,
                                     final JsValue value,
                                     final JsPath path
    ) {
        final SpecError pair = error.stream()
                                    .findFirst()
                                    .get();

        Assertions.assertEquals(pair.error.code(),
                                code
        );

        Assertions.assertEquals(pair.error.value(),
                                value
        );

        Assertions.assertEquals(pair.path,
                                path
        );
    }


}
