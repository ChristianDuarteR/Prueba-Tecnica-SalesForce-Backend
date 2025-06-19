package com.salesforce.skylogistics.controller;

import com.salesforce.skylogistics.dto.EntregaDTO;
import com.salesforce.skylogistics.exception.ClienteNotFoundException;
import com.salesforce.skylogistics.exception.EntregaInvalidDataException;
import com.salesforce.skylogistics.exception.EntregaNotFoundException;
import com.salesforce.skylogistics.model.Entrega;
import com.salesforce.skylogistics.service.EntregaServiceI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/entregas")
@Tag(name = "Entregas", description = "Operaciones relacionadas con entregas")
public class EntregaController {

    private final EntregaServiceI entregaServiceI;

    public EntregaController(@Autowired EntregaServiceI entregaServiceI) {
        this.entregaServiceI = entregaServiceI;
    }

    @Operation(summary = "Crear entrega para un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrega creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrega inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PostMapping("/clientes/{clienteId}")
    public ResponseEntity<Entrega> createEntrega(@PathVariable Long clienteId,
                                                 @RequestBody EntregaDTO entrega) {
        try {
            Entrega newEntrega = entregaServiceI.createEntrega(clienteId, entrega);
            URI location = URI.create("/api/entregas/" + newEntrega.getId());
            return ResponseEntity.created(location).body(newEntrega);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (EntregaInvalidDataException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener entregas por ID de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entregas encontradas"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/clientes/{clienteId}")
    public ResponseEntity<List<Entrega>> getEntregasByCliente(@PathVariable Long clienteId) {
        try {
            List<Entrega> entregas = entregaServiceI.getEntregasByCliente(clienteId);
            return ResponseEntity.ok(entregas);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar entrega existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Entrega actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrega inválidos"),
            @ApiResponse(responseCode = "404", description = "Entrega no encontrada")
    })
    @PutMapping("/{entregaId}")
    public ResponseEntity<Void> updateEntrega(@PathVariable Long entregaId,
                                              @RequestBody Entrega entrega) {
        try {
            entrega.setId(entregaId);
            entregaServiceI.updateEntrega(entrega);
            return ResponseEntity.accepted().build();
        } catch (EntregaNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (EntregaInvalidDataException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar entrega por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entrega eliminada"),
            @ApiResponse(responseCode = "404", description = "Entrega no encontrada")
    })
    @DeleteMapping("/{entregaId}")
    public ResponseEntity<Void> deleteEntrega(@PathVariable Long entregaId) {
        try {
            entregaServiceI.deleteEntrega(entregaId);
            return ResponseEntity.noContent().build();
        } catch (EntregaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}