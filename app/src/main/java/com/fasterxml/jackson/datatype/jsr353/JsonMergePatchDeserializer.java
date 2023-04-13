package com.fasterxml.jackson.datatype.jsr353;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.LogicalType;
import jakarta.json.Json;
import jakarta.json.JsonMergePatch;

import java.io.IOException;

public class JsonMergePatchDeserializer extends StdDeserializer<JsonMergePatch> {
    protected final JsonValueDeserializer jsonValueDeser;

    public JsonMergePatchDeserializer(JsonValueDeserializer jsonValueDeser) {
        super(JsonMergePatch.class);
        this.jsonValueDeser = jsonValueDeser;
    }

    public LogicalType logicalType() {
        return this.jsonValueDeser.logicalType();
    }

    public JsonMergePatch deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return Json.createMergePatch(this.jsonValueDeser.deserialize(p, ctxt));
    }
}
