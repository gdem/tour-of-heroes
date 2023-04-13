package com.fasterxml.jackson.datatype.jsr353;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;
import jakarta.json.*;
import jakarta.json.spi.JsonProvider;

import java.util.Collections;

public class JSR353Module extends SimpleModule {
    private static final long serialVersionUID = 1L;
    protected final JsonBuilderFactory _builderFactory;

    public JSR353Module() {
        this(JsonProvider.provider());
    }

    public JSR353Module(JsonProvider jsonProvider) {
        super("");
        this._builderFactory = jsonProvider.createBuilderFactory(Collections.emptyMap());
        final JsonValueDeserializer jsonValueDeser = new JsonValueDeserializer(JsonValue.class, this._builderFactory);
        final JsonPatchDeserializer jsonPatchDeser = new JsonPatchDeserializer(jsonValueDeser);
        final JsonMergePatchDeserializer jsonMergePatchDeser = new JsonMergePatchDeserializer(jsonValueDeser);
        this.addSerializer(JsonValue.class, new JsonValueSerializer());
        this.addSerializer(JsonPatch.class, new JsonPatchSerializer());
        this.addSerializer(JsonMergePatch.class, new JsonMergePatchSerializer());
        this.setDeserializers(new SimpleDeserializers() {
            public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) {
                if (type.isTypeOrSubTypeOf(JsonValue.class)) {
                    return type.hasRawClass(JsonValue.class) ? jsonValueDeser : new JsonValueDeserializer(type.getRawClass(), JSR353Module.this._builderFactory);
                } else if (JsonPatch.class.isAssignableFrom(type.getRawClass())) {
                    return jsonPatchDeser;
                } else {
                    return JsonMergePatch.class.isAssignableFrom(type.getRawClass()) ? jsonMergePatchDeser : null;
                }
            }

            public JsonDeserializer<?> findCollectionDeserializer(CollectionType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) {
                return type.hasRawClass(JsonArray.class) ? new JsonValueDeserializer(type.getRawClass(), JSR353Module.this._builderFactory) : null;
            }

            public JsonDeserializer<?> findMapDeserializer(MapType type, DeserializationConfig config, BeanDescription beanDesc, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) {
                return type.hasRawClass(JsonObject.class) ? new JsonValueDeserializer(type.getRawClass(), JSR353Module.this._builderFactory) : null;
            }

            public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
                return JsonValue.class.isAssignableFrom(valueType) || JsonPatch.class.isAssignableFrom(valueType) || JsonMergePatch.class.isAssignableFrom(valueType);
            }
        });
    }

    static class JsonMergePatchSerializer extends StdDelegatingSerializer {
        private static final long serialVersionUID = 1L;

        public JsonMergePatchSerializer() {
            super(new MergePatchConverter());
        }

        protected JsonMergePatchSerializer(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerialize) {
            super(converter, delegateType, delegateSerialize);
        }

        protected StdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
            return new JsonMergePatchSerializer(converter, delegateType, delegateSerializer);
        }

        static class MergePatchConverter extends StdConverter<JsonMergePatch, JsonValue> {
            MergePatchConverter() {
            }

            public JsonValue convert(JsonMergePatch value) {
                return value.toJsonValue();
            }
        }
    }

    static class JsonPatchSerializer extends StdDelegatingSerializer {
        private static final long serialVersionUID = 1L;

        public JsonPatchSerializer() {
            super(new PatchConverter());
        }

        protected JsonPatchSerializer(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerialize) {
            super(converter, delegateType, delegateSerialize);
        }

        protected StdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
            return new JsonPatchSerializer(converter, delegateType, delegateSerializer);
        }

        static class PatchConverter extends StdConverter<JsonPatch, JsonArray> {
            PatchConverter() {
            }

            public JsonArray convert(JsonPatch value) {
                return value.toJsonArray();
            }
        }
    }
}
