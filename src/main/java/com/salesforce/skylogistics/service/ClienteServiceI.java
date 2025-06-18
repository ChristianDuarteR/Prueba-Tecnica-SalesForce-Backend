package com.salesforce.skylogistics.service;

import com.salesforce.skylogistics.exception.ClienteNotFoundException;
import com.salesforce.skylogistics.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClienteServiceI {

    List<Cliente> getClientes() throws ClienteNotFoundException;

    Cliente getCliente(String email) throws  ClienteNotFoundException;

    Cliente createCliente(Cliente cliente);

    Cliente updateCliente(Cliente cliente);

    void deleteCliente(Cliente cliente);

}
