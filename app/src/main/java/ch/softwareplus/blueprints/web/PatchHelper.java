package ch.softwareplus.blueprints.web;

import ch.softwareplus.blueprints.web.exception.UnprocessableEntityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonPatch;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.Set;

@Component
@RequiredArgsConstructor
public class PatchHelper {

    private final ObjectMapper mapper;

    private final Validator validator;

    /**
     * Performs a JSON Patch operation.
     *
     * @param patch      JSON Patch document
     * @param targetBean object that will be patched
     * @param beanClass  class of the object the will be patched
     * @param <T>
     * @return patched object
     */
    public <T> T patch(JsonPatch patch, T targetBean, Class<T> beanClass) {
        var target = mapper.convertValue(targetBean, JsonStructure.class);
        var patched = applyPatch(patch, target);
        return convertAndValidate(patched, beanClass);
    }

    /**
     * Performs a JSON Merge Patch operation
     *
     * @param mergePatch JSON Merge Patch document
     * @param targetBean object that will be patched
     * @param beanClass  class of the object the will be patched
     * @param <T>
     * @return patched object
     */
    public <T> T mergePatch(JsonMergePatch mergePatch, T targetBean, Class<T> beanClass) {
        var target = mapper.convertValue(targetBean, JsonValue.class);
        var patched = applyMergePatch(mergePatch, target);
        return convertAndValidate(patched, beanClass);
    }

    private JsonValue applyPatch(JsonPatch patch, JsonStructure target) {
        try {
            return patch.apply(target);
        } catch (Exception e) {
            throw new UnprocessableEntityException(e);
        }
    }

    private JsonValue applyMergePatch(JsonMergePatch mergePatch, JsonValue target) {
        try {
            return mergePatch.apply(target);
        } catch (Exception e) {
            throw new UnprocessableEntityException(e);
        }
    }

    private <T> T convertAndValidate(JsonValue jsonValue, Class<T> beanClass) {
        T bean = mapper.convertValue(jsonValue, beanClass);
        validate(bean);
        return bean;
    }

    private <T> void validate(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
