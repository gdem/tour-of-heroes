package ch.softwareplus.blueprints.web;

import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;

@UtilityClass
public final class PatchMediaType {

    public static final String APPLICATION_JSON_PATCH_VALUE = "application/json-patch+json";

    public static final String APPLICATION_MERGE_PATCH_VALUE = "application/merge-patch+json";

    public static final MediaType APPLICATION_JSON_PATCH;

    public static final MediaType APPLICATION_MERGE_PATCH;

    static {
        APPLICATION_JSON_PATCH = MediaType.valueOf(APPLICATION_JSON_PATCH_VALUE);
        APPLICATION_MERGE_PATCH = MediaType.valueOf(APPLICATION_MERGE_PATCH_VALUE);
    }
}