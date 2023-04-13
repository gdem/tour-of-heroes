package com.fasterxml.jackson.datatype.jsr353;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import jakarta.json.*;

import java.io.IOException;

public class JsonValueDeserializer extends StdDeserializer<JsonValue> {
    private static final long serialVersionUID = 1L;
    protected final JsonBuilderFactory _builderFactory;
    protected final boolean _forJsonValue;

    /** @deprecated */
    @Deprecated
    public JsonValueDeserializer(JsonBuilderFactory bf) {
        this(JsonValue.class, bf);
    }

    public JsonValueDeserializer(Class<?> target, JsonBuilderFactory bf) {
        super(target);
        this._builderFactory = bf;
        this._forJsonValue = target == JsonValue.class;
    }

    public LogicalType logicalType() {
        return LogicalType.Untyped;
    }

    public JsonValue deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Object v;
        switch (p.getCurrentToken()) {
            case START_OBJECT:
                v = this._deserializeObject(p, ctxt);
                break;
            case START_ARRAY:
                v = this._deserializeArray(p, ctxt);
                break;
            default:
                v = this._deserializeScalar(p, ctxt);
        }

        if (!this._forJsonValue && !this.handledType().isAssignableFrom(v.getClass())) {
            ctxt.reportInputMismatch(this.handledType(), "Expected %s, but encountered %s Value", new Object[]{ClassUtil.getClassDescription(this.handledType()), ((JsonValue)v).getValueType().toString()});
        }

        return (JsonValue)v;
    }

    public JsonValue getNullValue(DeserializationContext ctxt) {
        return this._forJsonValue ? JsonValue.NULL : null;
    }

    public JsonValue getAbsentValue(DeserializationContext ctxt) {
        return null;
    }

    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeser) throws IOException {
        return typeDeser.deserializeTypedFromScalar(p, ctxt);
    }

    protected JsonObject _deserializeObject(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonObjectBuilder b = this._builderFactory.createObjectBuilder();

        while(p.nextToken() != JsonToken.END_OBJECT) {
            String name = p.getCurrentName();
            JsonToken t = p.nextToken();
            switch (t) {
                case START_OBJECT:
                    b.add(name, this._deserializeObject(p, ctxt));
                    break;
                case START_ARRAY:
                    b.add(name, this._deserializeArray(p, ctxt));
                    break;
                case VALUE_FALSE:
                    b.add(name, false);
                    break;
                case VALUE_TRUE:
                    b.add(name, true);
                    break;
                case VALUE_NULL:
                    b.addNull(name);
                    break;
                case VALUE_NUMBER_FLOAT:
                    if (p.getNumberType() == NumberType.BIG_DECIMAL) {
                        b.add(name, p.getDecimalValue());
                    } else {
                        b.add(name, p.getDoubleValue());
                    }
                    break;
                case VALUE_NUMBER_INT:
                    switch (p.getNumberType()) {
                        case LONG:
                            b.add(name, p.getLongValue());
                            continue;
                        case INT:
                            b.add(name, p.getIntValue());
                            continue;
                        default:
                            b.add(name, p.getBigIntegerValue());
                            continue;
                    }
                case VALUE_STRING:
                    b.add(name, p.getText());
                    break;
                case VALUE_EMBEDDED_OBJECT:
                    Object ob = p.getEmbeddedObject();
                    if (ob instanceof byte[]) {
                        String b64 = ctxt.getBase64Variant().encode((byte[])((byte[])ob), false);
                        b.add(name, b64);
                        break;
                    }

                    return (JsonObject)ctxt.handleUnexpectedToken(JsonObject.class, p);
                default:
                    return (JsonObject)ctxt.handleUnexpectedToken(JsonObject.class, p);
            }
        }

        return b.build();
    }

    protected JsonArray _deserializeArray(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonArrayBuilder b = this._builderFactory.createArrayBuilder();

        JsonToken t;
        while((t = p.nextToken()) != JsonToken.END_ARRAY) {
            switch (t) {
                case START_OBJECT:
                    b.add(this._deserializeObject(p, ctxt));
                    break;
                case START_ARRAY:
                    b.add(this._deserializeArray(p, ctxt));
                    break;
                case VALUE_FALSE:
                    b.add(false);
                    break;
                case VALUE_TRUE:
                    b.add(true);
                    break;
                case VALUE_NULL:
                    b.addNull();
                    break;
                case VALUE_NUMBER_FLOAT:
                    if (p.getNumberType() == NumberType.BIG_DECIMAL) {
                        b.add(p.getDecimalValue());
                    } else {
                        b.add(p.getDoubleValue());
                    }
                    break;
                case VALUE_NUMBER_INT:
                    switch (p.getNumberType()) {
                        case LONG:
                            b.add(p.getLongValue());
                            continue;
                        case INT:
                            b.add(p.getIntValue());
                            continue;
                        default:
                            b.add(p.getBigIntegerValue());
                            continue;
                    }
                case VALUE_STRING:
                    b.add(p.getText());
                    break;
                default:
                    return (JsonArray)ctxt.handleUnexpectedToken(JsonArray.class, p);
            }
        }

        return b.build();
    }

    protected JsonValue _deserializeScalar(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonArrayBuilder b;
        switch (p.getCurrentToken()) {
            case VALUE_FALSE:
                return JsonValue.FALSE;
            case VALUE_TRUE:
                return JsonValue.TRUE;
            case VALUE_NULL:
                return JsonValue.NULL;
            case VALUE_NUMBER_FLOAT:
                b = this._builderFactory.createArrayBuilder();
                if (p.getNumberType() == NumberType.BIG_DECIMAL) {
                    return (JsonValue)b.add(p.getDecimalValue()).build().get(0);
                }

                return (JsonValue)b.add(p.getDoubleValue()).build().get(0);
            case VALUE_NUMBER_INT:
                b = this._builderFactory.createArrayBuilder();
                switch (p.getNumberType()) {
                    case LONG:
                        return (JsonValue)b.add(p.getLongValue()).build().get(0);
                    case INT:
                        return (JsonValue)b.add(p.getIntValue()).build().get(0);
                    default:
                        return (JsonValue)b.add(p.getBigIntegerValue()).build().get(0);
                }
            case VALUE_STRING:
                return (JsonValue)this._builderFactory.createArrayBuilder().add(p.getText()).build().get(0);
            case VALUE_EMBEDDED_OBJECT:
                return (JsonValue)ctxt.handleUnexpectedToken(JsonValue.class, p);
            default:
                return (JsonValue)ctxt.handleUnexpectedToken(JsonValue.class, p);
        }
    }
}
