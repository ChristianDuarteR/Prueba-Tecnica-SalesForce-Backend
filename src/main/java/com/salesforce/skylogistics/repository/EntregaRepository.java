package com.salesforce.skylogistics.repository;

import com.salesforce.skylogistics.model.Cliente;
import com.salesforce.skylogistics.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    List<Entrega> getEntregaByCliente(Cliente cliente);

    List<Entrega> getEntregasByClienteId(Long clienteId);
}
