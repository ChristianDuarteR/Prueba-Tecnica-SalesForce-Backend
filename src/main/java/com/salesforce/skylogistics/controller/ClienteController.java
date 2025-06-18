package com.salesforce.skylogistics.controller;

import com.salesforce.skylogistics.exception.ClienteInvalidDataException;
import com.salesforce.skylogistics.exception.ClienteNotFoundException;
import com.salesforce.skylogistics.model.Cliente;
import com.salesforce.skylogistics.service.ClienteServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController("api/v1/clientes")
public class ClienteController {

    private final ClienteServiceI clienteServiceI;

    public ClienteController(@Autowired ClienteServiceI clienteServiceI) {
        this.clienteServiceI = clienteServiceI;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes(){
        try {
            List<Cliente> clientes = clienteServiceI.getClientes();
            return ResponseEntity.ok(clientes);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<Cliente> getCliente( @PathVariable String email) {
        try{
            Cliente cliente = clienteServiceI.getCliente(email);
            return ResponseEntity.ok(cliente);
        }catch (ClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        try {
            Cliente newCliente = clienteServiceI.createCliente(cliente);
            URI location = URI.create("/api/clientes/" + newCliente.getId());
            return ResponseEntity.created(location).build();
        } catch (ClienteInvalidDataException e) {
            return ResponseEntity.badRequest().build();
        }
    }

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
