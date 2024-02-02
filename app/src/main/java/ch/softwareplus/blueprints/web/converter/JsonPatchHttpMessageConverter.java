package ch.softwareplus.blueprints.web.converter;

import ch.softwareplus.blueprints.web.PatchMediaType;
import jakarta.json.Json;
import jakarta.json.JsonPatch;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

/**
 * HTTP message converter for {@link JsonPatch}.
 * <p>
 * Only supports {@code application/json-patch+json} media type.
 */
@Component
public class JsonPatchHttpMessageConverter extends AbstractHttpMessageConverter<JsonPatch> {

    public JsonPatchHttpMessageConverter() {
        super(PatchMediaType.APPLICATION_JSON_PATCH);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return JsonPatch.class.isAssignableFrom(clazz);
    }

    @Override
    protected JsonPatch readInternal(Class<? extends JsonPatch> clazz, HttpInputMessage inputMessage)
            throws HttpMessageNotReadableException {

        try (JsonReader reader = Json.createReader(inputMessage.getBody())) {
            return Json.createPatch(reader.readArray());
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(e.getMessage(), inputMessage);
        }
    }

    @Override
    protected void writeInternal(JsonPatch jsonPatch, HttpOutputMessage outputMessage)
            throws HttpMessageNotWritableException {

        try (JsonWriter writer = Json.createWriter(outputMessage.getBody())) {
            writer.write(jsonPatch.toJsonArray());
        } catch (Exception e) {
            throw new HttpMessageNotWritableException(e.getMessage(), e);
        }
    }
}