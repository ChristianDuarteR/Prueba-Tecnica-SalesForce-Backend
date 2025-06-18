package com.salesforce.skylogistics.service;

import com.salesforce.skylogistics.dto.EntregaDTO;
import com.salesforce.skylogistics.model.Entrega;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface EntregaServiceI {

    Entrega createEntrega(Long clienteId, EntregaDTO entrega);

    List<Entrega> getEntregasByCliente(Long clienteId);

    Entrega updateEntrega(Entrega entrega);

    void deleteEntrega(Long entregaId);
}
