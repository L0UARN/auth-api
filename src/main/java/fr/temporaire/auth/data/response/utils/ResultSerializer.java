package fr.temporaire.auth.data.response.utils;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Custom serializer for {@link Result}.
 * This serializer will choose to serialize either the success data or the error based on the success flag.
 */
@JsonComponent
public class ResultSerializer extends JsonSerializer<Result<?, ?>> {

    /**
     * Serializes the {@link Result} object.
     * If the operation was successful, it serializes the success data.
     * If the operation failed, it serializes the error information.
     *
     * @param result The Result object to serialize.
     * @param jsonGenerator The JsonGenerator used for writing JSON content.
     * @param serializerProvider The provider that can be used to get serializers for other types.
     * @throws IOException If an I/O error occurs during serialization.
     */
    @Override
    public void serialize(Result<?, ?> result, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (result.isSuccess()) {
            // Serialize success data
            serializerProvider.defaultSerializeValue(result.getData(), jsonGenerator);
        } else {
            // Serialize error data
            serializerProvider.defaultSerializeValue(result.getError(), jsonGenerator);
        }
    }
}