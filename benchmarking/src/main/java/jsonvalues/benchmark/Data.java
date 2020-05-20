package jsonvalues.benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Supplier;

public class Data {
    public static final Supplier<String> ARR_100;
    public static final Supplier<String> OBJ_100;
    public static final Supplier<String> ARR_1000;
    public static final Supplier<String> OBJ_1000;
    public static final Supplier<String> ARR_10000;
    public static final Supplier<String> OBJ_10000;
    public static final Supplier<String> ARR_100000;
    public static final Supplier<String> OBJ_100000;
    public static final Supplier<String> ARR_1000000;
    public static final Supplier<String> OBJ_1000000;

    static {
        OBJ_100 = () -> fileContent("obj100.json");
        ARR_100 = () -> fileContent("arr100.json");
        OBJ_1000 = () -> fileContent("obj1000.json");
        ARR_1000 = () -> fileContent("arr1000.json");
        OBJ_10000 = () -> fileContent("obj10000.json");
        ARR_10000 = () -> fileContent("arr10000.json");
        OBJ_100000 = () -> fileContent("obj100000.json");
        ARR_100000 = () -> fileContent("arr100000.json");
        OBJ_1000000 = () -> fileContent("obj1000000.json");
        ARR_1000000 = () -> fileContent("arr1000000.json");
    }

    private Data() {
    }

    private static String fileContent(String name) {
        final InputStream stream = Data.class.getClassLoader()
                                             .getResourceAsStream(name);

        try {
            return fromStream(stream);
        } catch (IOException e) {
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
}
