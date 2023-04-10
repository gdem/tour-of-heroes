package ch.softwareplus.blueprints.web;

import ch.softwareplus.blueprints.hero.api.HeroNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

/**
 * Enable RFC 7807 responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HeroNotFoundException.class)
    ProblemDetail handleBookmarkNotFoundException(HeroNotFoundException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Hero Not Found");
        problemDetail.setType(URI.create("https://www.software-plus.ch/errors/not-found"));
        return problemDetail;
    }
}