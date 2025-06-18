package com.salesforce.skylogistics.service.imp;

import com.salesforce.skylogistics.exception.ClienteInvalidDataException;
import com.salesforce.skylogistics.exception.ClienteNotFoundException;
import com.salesforce.skylogistics.model.Cliente;
import com.salesforce.skylogistics.repository.ClienteRepository;
import com.salesforce.skylogistics.service.ClienteServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService implements ClienteServiceI {

    private final  ClienteRepository clienteRepository;

    public ClienteService(@Autowired ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    @Override
    public List<Cliente> getClientes() throws ClienteNotFoundException {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            throw ClienteNotFoundException.general("No se encontrar)on clientes en la base de datos");
        }
        return clientes;
    }

    @Override
    public Cliente getCliente(String email) throws ClienteNotFoundException {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> ClienteNotFoundException.porId("Cliente no encontrado con email: " + email));
    }

    @Override
    public Cliente createCliente(Cliente cliente) {
        clienteRepository.findByEmail(cliente.getEmail()).ifPresent(c -> {
            throw ClienteInvalidDataException.badRequest(
                    "Ya existe un cliente con email: " + cliente.getEmail()
            );
        });

        clienteRepository.save(cliente);
        return cliente;
    }

    @Override
    public Cliente updateCliente(Cliente cliente) {
        Cliente clienteActualizado = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> ClienteNotFoundException.porId(String.valueOf(cliente.getId())));

        clienteActualizado.setNombre(cliente.getNombre());
        clienteActualizado.setEmail(cliente.getEmail());

        return clienteRepository.save(clienteActualizado);
    }

    @Override
    public void deleteCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("El cliente o su ID no pueden ser nulos");
        }
        if (!clienteRepository.existsById(cliente.getId())) {
            throw ClienteNotFoundException.porId(String.valueOf(cliente.getId()));
        }
        clienteRepository.deleteById(cliente.getId());
    }
}