package com.msd.erp.application.validationTests;

import com.msd.erp.application.validations.DomainValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DomainValidationServiceTest {

    private Validator mockValidator;
    private DomainValidationService domainValidationService;

    @BeforeEach
    void setUp() {
        mockValidator = mock(Validator.class);
        domainValidationService = new DomainValidationService(mockValidator);
    }

    @Test
    void validateEntity_shouldPassWhenNoViolations() {
        Object validEntity = new Object();

        when(mockValidator.validate(validEntity)).thenReturn(Set.of());

        assertDoesNotThrow(() -> domainValidationService.validateEntity(validEntity));

        verify(mockValidator).validate(validEntity);
    }

    @Test
    void validateEntity_shouldThrowExceptionWhenViolationsExist() {
        Object invalidEntity = new Object();
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);

        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("fieldName");
        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(violation.getMessage()).thenReturn("must not be null");

        Set<ConstraintViolation<Object>> violations = Set.of(violation);

        when(mockValidator.validate(invalidEntity)).thenReturn(violations);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> domainValidationService.validateEntity(invalidEntity)
        );

        assertTrue(exception.getMessage().contains("Validation errors"));
        assertTrue(exception.getMessage().contains("fieldName: must not be null"));

        verify(mockValidator).validate(invalidEntity);
    }
}