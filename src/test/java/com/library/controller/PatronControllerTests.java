package com.library.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;

import com.library.config.SecurityConfig;
import com.library.model.Patron;
import com.library.security.JwtUtils;
import com.library.service.PatronDetailsService;
import com.library.service.PatronService;
import com.library.exception.PatronNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PatronController.class)
@Import({JwtUtils.class, SecurityConfig.class})
public class PatronControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;
    @MockBean
    private PatronDetailsService patronDetailsService; // Ensure this is correctly mocked

    private Patron patron;

    @BeforeEach
    public void setup() {
        patron = new Patron(1L, "John Doe", "555-5555", "john.doe@example.com", "password123");
    }

    @Test
    public void testGetAllPatrons() throws Exception {
        given(patronService.findAllPatrons()).willReturn(Arrays.asList(patron));

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void testGetPatronByIdSuccess() throws Exception {
        given(patronService.findPatronById(1L)).willReturn(patron);

        mockMvc.perform(get("/api/patrons/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPatronByIdNotFound() throws Exception {
        given(patronService.findPatronById(anyLong())).willThrow(new PatronNotFoundException("Not found"));

        mockMvc.perform(get("/api/patrons/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));
    }

    @Test
    public void testDeletePatronSuccess() throws Exception {
        willDoNothing().given(patronService).deletePatron(1L);

        mockMvc.perform(delete("/api/patrons/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePatronNotFound() throws Exception {
        willThrow(new PatronNotFoundException("Not found")).given(patronService).deletePatron(1L);

        mockMvc.perform(delete("/api/patrons/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
