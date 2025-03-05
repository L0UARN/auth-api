package fr.temporaire.auth.data.response.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ResultSerializer extends JsonSerializer<Result<?, ?>> {
    @Override
    public void serialize(Result<?, ?> result, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (result.isSuccess()) {
            serializerProvider.defaultSerializeValue(result.getData(), jsonGenerator);
        } else {
            serializerProvider.defaultSerializeValue(result.getError(), jsonGenerator);
        }
    }
}
