package com.salesforce.skylogistics.controller;

import com.salesforce.skylogistics.dto.EntregaDTO;
import com.salesforce.skylogistics.exception.ClienteNotFoundException;
import com.salesforce.skylogistics.exception.EntregaInvalidDataException;
import com.salesforce.skylogistics.exception.EntregaNotFoundException;
import com.salesforce.skylogistics.model.Entrega;
import com.salesforce.skylogistics.service.EntregaServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/entregas")
public class EntregaController {

    private final EntregaServiceI entregaServiceI;

    public EntregaController(@Autowired EntregaServiceI entregaServiceI) {
        this.entregaServiceI = entregaServiceI;
    }

    @PostMapping("/clientes/{clienteId}")
    public ResponseEntity<Void> createEntrega(@PathVariable Long clienteId,
                                              @RequestBody EntregaDTO entrega) {
        try {
            Entrega newEntrega = entregaServiceI.createEntrega(clienteId, entrega);
            URI location = URI.create("/api/entregas/" + newEntrega.getId());
            return ResponseEntity.created(location).build();

        } catch (ClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (EntregaInvalidDataException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/clientes/{clienteId}")
    public ResponseEntity<List<Entrega>> getEntregasByCliente(@PathVariable Long clienteId) {
        try {
            List<Entrega> entregas = entregaServiceI.getEntregasByCliente(clienteId);
            return ResponseEntity.ok(entregas);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

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
