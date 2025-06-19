package com.salesforce.skylogistics.service.imp;

import com.salesforce.skylogistics.dto.EntregaDTO;
import com.salesforce.skylogistics.exception.ClienteNotFoundException;
import com.salesforce.skylogistics.exception.EntregaNotFoundException;
import com.salesforce.skylogistics.model.Cliente;
import com.salesforce.skylogistics.model.Entrega;
import com.salesforce.skylogistics.repository.ClienteRepository;
import com.salesforce.skylogistics.repository.EntregaRepository;
import com.salesforce.skylogistics.service.EntregaServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntregaService implements EntregaServiceI {

    private final EntregaRepository  entregaRepository;
    private final ClienteRepository  clienteRepository;


    public EntregaService(@Autowired EntregaRepository entregaRepository,
                          @Autowired ClienteRepository clienteRepository) {
        this.entregaRepository = entregaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Entrega createEntrega(Long clienteId, EntregaDTO entrega) {

        Entrega mappedEntrega = entrega.toEntity();
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> ClienteNotFoundException.porId(String.valueOf(clienteId)));

        mappedEntrega.setCliente(cliente);
        cliente.getEntregas().add(mappedEntrega);

        return entregaRepository.save(mappedEntrega);
    }


    @Override
    public List<Entrega> getEntregasByCliente(Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw ClienteNotFoundException.porId(String.valueOf(clienteId));
        }
        return entregaRepository.getEntregasByClienteId(clienteId);
    }

    @Override
    public Entrega updateEntrega(Entrega entrega) {

        Entrega existente = entregaRepository.findById(entrega.getId())
                .orElseThrow(() -> EntregaNotFoundException.porId(String.valueOf(entrega.getId())));

        existente.setDescripcion(entrega.getDescripcion());
        existente.setFechaEntrega(entrega.getFechaEntrega());

        return entregaRepository.save(existente);
    }

    @Override
    public void deleteEntrega(Long entregaId) {

        if (!entregaRepository.existsById(entregaId)) {
            throw EntregaNotFoundException.porId(String.valueOf(entregaId));
        }
        entregaRepository.deleteById(entregaId);
    }
}
