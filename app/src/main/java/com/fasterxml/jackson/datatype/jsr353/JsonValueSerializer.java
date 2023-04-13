package com.fasterxml.jackson.datatype.jsr353;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.json.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


public class JsonValueSerializer extends StdSerializer<JsonValue> {
    private static final long serialVersionUID = 1L;

    public JsonValueSerializer() {
        super(JsonValue.class);
    }

    public void serialize(JsonValue value, JsonGenerator g, SerializerProvider provider) throws IOException {
        switch (value.getValueType()) {
            case ARRAY:
                g.writeStartArray();
                this.serializeArrayContents((JsonArray) value, g, provider);
                g.writeEndArray();
                break;
            case OBJECT:
                g.writeStartObject(value);
                this.serializeObjectContents((JsonObject) value, g, provider);
                g.writeEndObject();
                break;
            default:
                this.serializeScalar(value, g, provider);
        }

    }

    public void serializeWithType(JsonValue value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        g.setCurrentValue(value);
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer.typeId(value, JsonValue.class, JsonToken.VALUE_EMBEDDED_OBJECT));
        this.serialize(value, g, provider);
        typeSer.writeTypeSuffix(g, typeIdDef);
    }

    protected void serializeScalar(JsonValue value, JsonGenerator g, SerializerProvider provider) throws IOException {
        switch (value.getValueType()) {
            case FALSE:
                g.writeBoolean(false);
                break;
            case NULL:
                g.writeNull();
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) value;
                if (num.isIntegral()) {
                    g.writeNumber(num.longValue());
                } else {
                    g.writeNumber(num.bigDecimalValue());
                }
                break;
            case STRING:
                g.writeString(((JsonString) value).getString());
                break;
            case TRUE:
                g.writeBoolean(true);
        }

    }

    protected void serializeArrayContents(JsonArray values, JsonGenerator g, SerializerProvider provider) throws IOException {
        if (!values.isEmpty()) {
            Iterator var4 = values.iterator();

            while (var4.hasNext()) {
                JsonValue value = (JsonValue) var4.next();
                this.serialize(value, g, provider);
            }
        }

    }

    protected void serializeObjectContents(JsonObject ob, JsonGenerator g, SerializerProvider provider) throws IOException {
        if (!ob.isEmpty()) {
            Iterator var4 = ob.entrySet().iterator();

            while (var4.hasNext()) {
                Map.Entry<String, JsonValue> entry = (Map.Entry) var4.next();
                g.writeFieldName((String) entry.getKey());
                this.serialize((JsonValue) entry.getValue(), g, provider);
            }
        }

    }
}
