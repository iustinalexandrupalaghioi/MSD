package com.msd.erp.application.validations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DomainValidationService {

    private final Validator validator;

    public <T> void validateEntity(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Validation errors: ");
            violations.forEach(v -> sb.append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("; "));
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
