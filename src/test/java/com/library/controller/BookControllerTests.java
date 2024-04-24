package com.library.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.config.SecurityConfig;
import com.library.exception.BookNotFoundException;
import com.library.model.Book;
import com.library.security.JwtUtils;
import com.library.service.BookService;
import com.library.service.PatronDetailsService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@Import({JwtUtils.class,SecurityConfig.class})
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book book;

    @MockBean
    private PatronDetailsService patronDetailsService;

    @BeforeEach
    public void setup() {
        book = new Book(1L, "bookTitle1", "Nardeen", 2018, "999");
    }

    @Test
    public void testGetAllBooks() throws Exception {
        given(bookService.findAllBooks()).willReturn(Arrays.asList(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetBookByIdSuccess() throws Exception {
        given(bookService.findBookById(1L)).willReturn(book);

        mockMvc.perform(get("/api/books/{id}", 1))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetBookByIdNotFound() throws Exception {
        given(bookService.findBookById(anyLong())).willThrow(new BookNotFoundException("Not found"));

        mockMvc.perform(get("/api/books/{id}", 1))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testAddBook() throws Exception {
        given(bookService.saveBook(book)).willReturn(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateBookSuccess() throws Exception {
        given(bookService.updateBook(eq(1L), any(Book.class))).willReturn(book);
        mockMvc.perform(put("/api/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk());

    }

    @Test
    public void testDeleteBookSuccess() throws Exception {
        willDoNothing().given(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteBookNotFound() throws Exception {
        willThrow(new BookNotFoundException("Not found")).given(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/{id}", 1))
                .andExpect(status().isNotFound());

    }
}
