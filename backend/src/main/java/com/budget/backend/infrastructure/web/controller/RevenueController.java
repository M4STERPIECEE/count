package com.budget.backend.infrastructure.web.controller;

import com.budget.backend.application.dto.RevenueDTO;
import com.budget.backend.application.port.input.RevenueServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/revenues")
@RequiredArgsConstructor
@Tag(name = "Revenues", description = "API de gestion des revenus")
public class RevenueController {

    private final RevenueServicePort revenueService;

    @PostMapping
    @Operation(summary = "Créer un nouveau revenu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Revenu créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<RevenueDTO> createRevenue(@Valid @RequestBody RevenueDTO revenueDTO) {
        RevenueDTO created = revenueService.createRevenue(revenueDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un revenu par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revenu trouvé"),
            @ApiResponse(responseCode = "404", description = "Revenu non trouvé")
    })
    public ResponseEntity<RevenueDTO> getRevenueById(@PathVariable UUID id) {
        return ResponseEntity.ok(revenueService.getRevenueById(id));
    }

    @GetMapping
    @Operation(summary = "Lister tous les revenus")
    public ResponseEntity<List<RevenueDTO>> getAllRevenues() {
        return ResponseEntity.ok(revenueService.getAllRevenues());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un revenu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revenu mis à jour"),
            @ApiResponse(responseCode = "404", description = "Revenu non trouvé")
    })
    public ResponseEntity<RevenueDTO> updateRevenue(@PathVariable UUID id,
                                                    @Valid @RequestBody RevenueDTO revenueDTO) {
        return ResponseEntity.ok(revenueService.updateRevenue(id, revenueDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un revenu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Revenu supprimé"),
            @ApiResponse(responseCode = "404", description = "Revenu non trouvé")
    })
    public ResponseEntity<Void> deleteRevenue(@PathVariable UUID id) {
        revenueService.deleteRevenue(id);
        return ResponseEntity.noContent().build();
    }
}
