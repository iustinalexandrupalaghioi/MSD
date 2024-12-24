package com.msd.erp.application.workflowTests;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.msd.erp.application.services.RelationService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Relation;
import com.msd.erp.domain.RelationType;
import com.msd.erp.infrastructure.repositories.RelationRepository;

class RelationServiceTest {

    @Mock
    private RelationRepository relationRepository;

    @Mock
    private DomainValidationService validationService;

    @InjectMocks
    private RelationService relationService;

    private Relation relation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        relation = new Relation();
        relation.setRelationid(1L);
        relation.setName("Test Supplier");
        relation.setCountry("Test Country");
        relation.setAddress("123 Test Street");
        relation.setEmail("test@example.com");
        relation.setPhonenumber("123456789");
        relation.setRelationtype(RelationType.SUPPLIER);
    }

    @Test
    void createRelation_ShouldSaveRelation() {
        when(relationRepository.save(relation)).thenReturn(relation);

        Relation result = relationService.createRelation(relation);

        assertNotNull(result);
        assertEquals(relation.getRelationid(), result.getRelationid());
        verify(validationService).validateEntity(relation);
        verify(relationRepository).save(relation);
    }

    @Test
    void getRelationById_ShouldReturnRelation() {
        when(relationRepository.findById(1L)).thenReturn(Optional.of(relation));

        Optional<Relation> result = relationService.getRelationById(1L);

        assertTrue(result.isPresent());
        assertEquals(relation.getRelationid(), result.get().getRelationid());
        verify(relationRepository).findById(1L);
    }

    @Test
    void updateRelation_ShouldUpdateExistingRelation() {
        Relation updatedRelation = new Relation();
        updatedRelation.setName("Updated Supplier");
        updatedRelation.setCountry("Updated Country");
        updatedRelation.setAddress("456 Updated Street");
        updatedRelation.setEmail("updated@example.com");
        updatedRelation.setPhonenumber("987654321");
        updatedRelation.setRelationtype(RelationType.CUSTOMER);

        when(relationRepository.findById(1L)).thenReturn(Optional.of(relation));
        when(relationRepository.save(relation)).thenReturn(relation);

        Optional<Relation> result = relationService.updateRelation(1L, updatedRelation);

        assertTrue(result.isPresent());
        assertEquals("Updated Supplier", result.get().getName());
        assertEquals("Updated Country", result.get().getCountry());
        assertEquals("456 Updated Street", result.get().getAddress());
        assertEquals("updated@example.com", result.get().getEmail());
        assertEquals("987654321", result.get().getPhonenumber());
        assertEquals(RelationType.CUSTOMER, result.get().getRelationtype());
        verify(validationService).validateEntity(relation);
        verify(relationRepository).save(relation);
    }

    @Test
    void deleteRelation_ShouldDeleteRelation() {
        when(relationRepository.existsById(1L)).thenReturn(true);

        relationService.deleteRelation(1L);

        verify(relationRepository).deleteById(1L);
    }

    @Test
    void deleteRelation_ShouldThrowExceptionIfNotFound() {
        when(relationRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> relationService.deleteRelation(1L));

        assertEquals("Relation with ID 1 does not exist.", exception.getMessage());
    }
}
