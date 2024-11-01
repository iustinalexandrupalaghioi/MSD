package com.msd.erp.web;

import com.msd.erp.domain.Relation;
import com.msd.erp.application.services.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relations")
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;

    @PostMapping
    public ResponseEntity<Relation> createRelation(@RequestBody Relation relation) {
        Relation createdRelation = relationService.createRelation(relation);
        return ResponseEntity.ok(createdRelation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Relation> getRelationById(@PathVariable Long id) {
        return relationService.getRelationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Relation>> getAllRelations() {
        List<Relation> relations = relationService.getAllRelations();
        return ResponseEntity.ok(relations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Relation> updateRelation(@PathVariable Long id, @RequestBody Relation relation) {
        return relationService.updateRelation(id, relation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelation(@PathVariable Long id) {
        relationService.deleteRelation(id);
        return ResponseEntity.noContent().build();
    }
}
