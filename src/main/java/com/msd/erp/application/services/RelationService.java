package com.msd.erp.application.services;

import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Relation;
import com.msd.erp.infrastructure.repositories.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationService {

    private final RelationRepository relationRepository;
    private final DomainValidationService validationService;

    @Transactional
    public Relation createRelation(Relation relation) {
        validationService.validateEntity(relation);
        return relationRepository.save(relation);
    }

    public List<Relation> getAllRelations() {
        return relationRepository.findAll();
    }

    public Optional<Relation> getRelationById(Long id) {
        return relationRepository.findById(id);
    }

    @Transactional
    public Optional<Relation> updateRelation(Long id, Relation updatedRelation) {
        return relationRepository.findById(id).map(existingRelation -> {
            existingRelation.setName(updatedRelation.getName());
            existingRelation.setCountry(updatedRelation.getCountry());
            existingRelation.setAddress(updatedRelation.getAddress());
            existingRelation.setEmail(updatedRelation.getEmail());
            existingRelation.setPhonenumber(updatedRelation.getPhonenumber());
            existingRelation.setRelationtype(updatedRelation.getRelationtype());

            validationService.validateEntity(existingRelation);
            return relationRepository.save(existingRelation);
        });
    }

    @Transactional
    public void deleteRelation(Long id) {
        if (relationRepository.existsById(id)) {
            relationRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Relation with ID " + id + " does not exist.");
        }
    }

    public boolean relationExists(Long id) {
        return relationRepository.existsById(id);
    }

    public Optional<Relation> getRelationByName(String name) {
        return relationRepository.findByName(name);
    }
}