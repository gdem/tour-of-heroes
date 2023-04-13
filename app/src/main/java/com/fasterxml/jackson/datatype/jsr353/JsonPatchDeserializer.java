package com.fasterxml.jackson.datatype.jsr353;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.type.LogicalType;
import jakarta.json.Json;
import jakarta.json.JsonPatch;

import java.io.IOException;

public class JsonPatchDeserializer extends StdDeserializer<JsonPatch> {
    protected final JsonValueDeserializer jsonValueDeser;

    public JsonPatchDeserializer(JsonValueDeserializer jsonValueDeser) {
        super(JsonPatch.class);
        this.jsonValueDeser = jsonValueDeser;
    }

    public LogicalType logicalType() {
        return this.jsonValueDeser.logicalType();
    }

    public JsonPatch deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() != JsonToken.START_ARRAY) {
            throw InvalidFormatException.from(p, "JSON patch has to be an array of objects", p.getText(), this.handledType());
        } else {
            return Json.createPatch(this.jsonValueDeser._deserializeArray(p, ctxt));
        }
    }
}