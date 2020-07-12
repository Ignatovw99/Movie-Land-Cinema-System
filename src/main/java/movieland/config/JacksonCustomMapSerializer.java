package movieland.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class JacksonCustomMapSerializer extends JsonSerializer<Map<?, ?>> {

    @Override
    public void serialize(Map<?, ?> map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (Map.Entry<?, ?> entry : map.entrySet()){
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("key", entry.getKey());
            jsonGenerator.writeObjectField("value", entry.getValue());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
