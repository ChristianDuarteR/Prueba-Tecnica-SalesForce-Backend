package com.salesforce.skylogistics.controller;

import com.salesforce.skylogistics.dto.EntregaDTO;
import com.salesforce.skylogistics.exception.ClienteInvalidDataException;
import com.salesforce.skylogistics.exception.ClienteNotFoundException;
import com.salesforce.skylogistics.exception.EntregaInvalidDataException;
import com.salesforce.skylogistics.exception.EntregaNotFoundException;
import com.salesforce.skylogistics.model.Cliente;
import com.salesforce.skylogistics.model.Entrega;
import com.salesforce.skylogistics.service.ClienteServiceI;
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
@RequestMapping("api/v1/clientes")
@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")
public class ClienteController {

    private final ClienteServiceI clienteServiceI;

    public ClienteController(@Autowired ClienteServiceI clienteServiceI) {
        this.clienteServiceI = clienteServiceI;
    }

    @Operation(summary = "Listar todos los clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron clientes")
    })
    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes(){
        try {
            List<Cliente> clientes = clienteServiceI.getClientes();
            return ResponseEntity.ok(clientes);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener cliente por email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{email}")
    public ResponseEntity<Cliente> getCliente(@PathVariable String email) {
        try {
            Cliente cliente = clienteServiceI.getCliente(email);
            return ResponseEntity.ok(cliente);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear un nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        try {
            Cliente newCliente = clienteServiceI.createCliente(cliente);
            URI location = URI.create("/api/clientes/" + newCliente.getId());
            return ResponseEntity.created(location).body(newCliente);
        } catch (ClienteInvalidDataException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping
    public ResponseEntity<Cliente> updateCliente(@RequestBody Cliente cliente){
        try {
            clienteServiceI.updateCliente(cliente);
            return ResponseEntity.accepted().build();
        } catch (ClienteInvalidDataException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}