package network.arkane.provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONUtil {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(final Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to encode to json");
        }
    }

    public static <T> T fromJson(String input, Class<T> fromClass) {
        try {
            return mapper.readValue(input, fromClass);
        } catch (final Exception ex) {
            throw new IllegalArgumentException("unable to decode from json");
        }
    }
}