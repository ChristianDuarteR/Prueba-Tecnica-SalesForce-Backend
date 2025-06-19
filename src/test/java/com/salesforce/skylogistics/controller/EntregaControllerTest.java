package com.salesforce.skylogistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.skylogistics.dto.EntregaDTO;
import com.salesforce.skylogistics.exception.ClienteNotFoundException;
import com.salesforce.skylogistics.exception.EntregaInvalidDataException;
import com.salesforce.skylogistics.exception.EntregaNotFoundException;
import com.salesforce.skylogistics.model.Entrega;
import com.salesforce.skylogistics.service.EntregaServiceI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EntregaController.class)
@AutoConfigureMockMvc(addFilters = false)
class EntregaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private EntregaServiceI entregaService;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @Test
    @DisplayName("POST /api/v1/entregas/clientes/{id} — 201 Created")
    void shouldCreateEntrega() throws Exception {
        EntregaDTO dto = new EntregaDTO();
        dto.setDescripcion("REF‑01");
        dto.setFechaEntrega(LocalDate.now());
        dto.setEstado("EN RUTA");
        dto.setDireccion("Calle 123");

        Entrega saved = new Entrega();
        saved.setId(99L);
        saved.setDescripcion(dto.getDescripcion());
        saved.setFechaEntrega(dto.getFechaEntrega());
        saved.setEstado(dto.getEstado());
        saved.setDireccion(dto.getDireccion());

        when(entregaService.createEntrega(eq(1L), any()))
                .thenReturn(saved);

        mvc.perform(post("/api/v1/entregas/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/entregas/99")))
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.descripcion", is("REF‑01")));
    }

    @Test
    @DisplayName("POST /api/v1/entregas/clientes/{id} — 404 si cliente no existe")
    void shouldReturn404WhenClienteNotFound() throws Exception {
        EntregaDTO dto = new EntregaDTO();

        when(entregaService.createEntrega(eq(1L), any()))
                .thenThrow(new ClienteNotFoundException("no cliente"));

        mvc.perform(post("/api/v1/entregas/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/entregas/clientes/{id} — 400 datos inválidos")
    void shouldReturn400WhenInvalidEntrega() throws Exception {
        EntregaDTO dto = new EntregaDTO();

        when(entregaService.createEntrega(eq(1L), any()))
                .thenThrow(new EntregaInvalidDataException("invalid"));

        mvc.perform(post("/api/v1/entregas/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/entregas/clientes/{id} — 200 OK con lista")
    void shouldListEntregasByCliente() throws Exception {
        Entrega e1 = new Entrega(1L, "A", LocalDate.now(), "BOG", "Dir 1", null);
        Entrega e2 = new Entrega(2L, "B", LocalDate.now(), "MED", "Dir 2", null);

        when(entregaService.getEntregasByCliente(1L)).thenReturn(List.of(e1, e2));

        mvc.perform(get("/api/v1/entregas/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET /api/v1/entregas/clientes/{id} — 404 si cliente no existe")
    void shouldReturn404WhenClienteNotFoundOnList() throws Exception {
        when(entregaService.getEntregasByCliente(1L))
                .thenThrow(new ClienteNotFoundException("no cliente"));

        mvc.perform(get("/api/v1/entregas/clientes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/entregas/{id} — 202 Accepted")
    void shouldUpdateEntrega() throws Exception {
        Entrega payload = new Entrega();
        payload.setDescripcion("ACTUALIZADA");

        mvc.perform(put("/api/v1/entregas/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isAccepted());

        verify(entregaService)
                .updateEntrega(ArgumentMatchers.argThat(e -> e.getId().equals(10L)));
    }

    @Test
    @DisplayName("PUT /api/v1/entregas/{id} — 404 si no existe")
    void shouldReturn404WhenUpdateNotFound() throws Exception {
        doThrow(new EntregaNotFoundException("no entrega"))
                .when(entregaService).updateEntrega(any());

        mvc.perform(put("/api/v1/entregas/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Entrega())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/entregas/{id} — 400 datos inválidos")
    void shouldReturn400WhenUpdateInvalid() throws Exception {
        doThrow(new EntregaInvalidDataException("bad"))
                .when(entregaService).updateEntrega(any());

        mvc.perform(put("/api/v1/entregas/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Entrega())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/v1/entregas/{id} — 204 No Content")
    void shouldDeleteEntrega() throws Exception {
        mvc.perform(delete("/api/v1/entregas/10"))
                .andExpect(status().isNoContent());

        verify(entregaService).deleteEntrega(10L);
    }

    @Test
    @DisplayName("DELETE /api/v1/entregas/{id} — 404 si no existe")
    void shouldReturn404WhenDeleteNotFound() throws Exception {
        doThrow(new EntregaNotFoundException("no entrega"))
                .when(entregaService).deleteEntrega(10L);

        mvc.perform(delete("/api/v1/entregas/10"))
                .andExpect(status().isNotFound());
    }
}
