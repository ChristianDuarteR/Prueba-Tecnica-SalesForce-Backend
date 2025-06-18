package com.salesforce.skylogistics.dto;


import com.salesforce.skylogistics.model.Entrega;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EntregaDTO {

    private String descripcion;

    private LocalDate fechaEntrega;

    private String estado;

    private String direccion;

    public Entrega toEntity() {
        Entrega entrega = new Entrega();
        entrega.setDescripcion(this.descripcion);
        entrega.setFechaEntrega(this.fechaEntrega);
        entrega.setEstado(this.estado);
        entrega.setDireccion(this.direccion);
        return  entrega;
    }
}