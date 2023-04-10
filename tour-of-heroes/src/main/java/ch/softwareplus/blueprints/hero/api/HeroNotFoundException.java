package ch.softwareplus.blueprints.hero.api;

import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Specific exception to indicate that hero could not be found.
 */
@Value
@RequiredArgsConstructor
public class HeroNotFoundException extends RuntimeException {
    private final Long id;
}
