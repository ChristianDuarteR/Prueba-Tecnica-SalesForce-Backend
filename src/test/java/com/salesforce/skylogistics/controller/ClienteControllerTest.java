package com.salesforce.skylogistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.skylogistics.exception.ClienteInvalidDataException;
import com.salesforce.skylogistics.exception.ClienteNotFoundException;
import com.salesforce.skylogistics.model.Cliente;
import com.salesforce.skylogistics.service.ClienteServiceI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ClienteServiceI clienteService;

    private final ObjectMapper mapper = new ObjectMapper();

    // ---------- GET /clientes (lista) ----------
    @Test
    @DisplayName("GET /api/v1/clientes — 200 OK con listado")
    void shouldReturnAllClientes() throws Exception {
        Cliente c1 = new Cliente();
        c1.setId(1L);
        c1.setNombre("Juan");
        c1.setEmail("juan@mail.com");

        Cliente c2 = new Cliente();
        c2.setId(2L);
        c2.setNombre("Ana");
        c2.setEmail("ana@mail.com");

        when(clienteService.getClientes()).thenReturn(List.of(c1, c2));

        mvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is("juan@mail.com")))
                .andExpect(jsonPath("$[1].nombre", is("Ana")));
    }

    @Test
    @DisplayName("GET /api/v1/clientes — 404 cuando no hay clientes")
    void shouldReturn404WhenNoClientes() throws Exception {
        when(clienteService.getClientes()).thenThrow(new ClienteNotFoundException("no data"));

        mvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isNotFound());
    }

    // ---------- GET /clientes/{email} ----------
    @Test
    @DisplayName("GET /api/v1/clientes/{email} — 200 OK")
    void shouldReturnClienteByEmail() throws Exception {
        Cliente c = new Cliente();
        c.setId(3L);
        c.setNombre("Juan");
        c.setEmail("juan@mail.com");

        when(clienteService.getCliente("juan@mail.com")).thenReturn(c);

        mvc.perform(get("/api/v1/clientes/juan@mail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.id", is(3)));
    }

    @Test
    @DisplayName("GET /api/v1/clientes/{email} — 404 si no existe")
    void shouldReturn404WhenEmailNotFound() throws Exception {
        when(clienteService.getCliente("foo@mail.com"))
                .thenThrow(new ClienteNotFoundException("not found"));

        mvc.perform(get("/api/v1/clientes/foo@mail.com"))
                .andExpect(status().isNotFound());
    }

    // ---------- POST /clientes ----------
    @Test
    @DisplayName("POST /api/v1/clientes — 201 Created")
    void shouldCreateCliente() throws Exception {
        Cliente payload = new Cliente(); // lo que recibimos
        payload.setNombre("Laura");
        payload.setEmail("laura@mail.com");

        Cliente saved = new Cliente();   // lo que devuelve el servicio
        saved.setId(5L);
        saved.setNombre("Laura");
        saved.setEmail("laura@mail.com");

        when(clienteService.createCliente(any(Cliente.class))).thenReturn(saved);

        mvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/clientes/5")))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.email", is("laura@mail.com")));
    }

    @Test
    @DisplayName("POST /api/v1/clientes — 400 Bad Request si datos inválidos")
    void shouldReturn400WhenInvalidCliente() throws Exception {
        Cliente payload = new Cliente();
        payload.setNombre("");              // nombre vacío para que sea inválido
        payload.setEmail("bad@mail");

        when(clienteService.createCliente(any(Cliente.class)))
                .thenThrow(new ClienteInvalidDataException("invalid"));

        mvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    // ---------- PUT /clientes ----------
    @Test
    @DisplayName("PUT /api/v1/clientes — 202 Accepted")
    void shouldUpdateCliente() throws Exception {
        Cliente payload = new Cliente();
        payload.setId(9L);
        payload.setNombre("Mario");
        payload.setEmail("mario@mail.com");

        mvc.perform(put("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isAccepted());

        verify(clienteService).updateCliente(ArgumentMatchers.refEq(payload));
    }

    @Test
    @DisplayName("PUT /api/v1/clientes — 400 Bad Request si datos inválidos")
    void shouldReturn400WhenUpdateInvalid() throws Exception {
        Cliente payload = new Cliente();
        payload.setId(9L);
        payload.setNombre(""); // inválido

        doThrow(new ClienteInvalidDataException("invalid"))
                .when(clienteService).updateCliente(any(Cliente.class));

        mvc.perform(put("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }
}