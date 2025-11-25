package org.globalds.mutantes.controller;

import org.globalds.mutantes.dto.DnaRequest;
import org.globalds.mutantes.dto.StatsResponse;
import org.globalds.mutantes.service.MutantService;
import org.globalds.mutantes.service.StatsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaRequest request) {

        boolean mutant = mutantService.analyzeDna(request.getDna());

        return mutant
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(403).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> stats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
