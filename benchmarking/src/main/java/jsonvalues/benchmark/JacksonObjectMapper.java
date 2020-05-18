package jsonvalues.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonObjectMapper {

    private static ObjectMapper mapper;


    public static ObjectMapper get() {
        if (mapper == null) mapper = new ObjectMapper();
        return mapper;
    }


}
