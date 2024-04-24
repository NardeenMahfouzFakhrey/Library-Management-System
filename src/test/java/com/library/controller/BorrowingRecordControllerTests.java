package com.library.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.library.config.SecurityConfig;
import com.library.exception.BookNotFoundException;
import com.library.security.JwtUtils;
import com.library.service.PatronDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.library.exception.BorrowingRecordNotAvailable;
import com.library.model.BorrowingRecord;
import com.library.service.BorrowingRecordService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BorrowingRecordController.class)
@Import({JwtUtils.class, SecurityConfig.class})
public class BorrowingRecordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordService borrowingRecordService;
    @MockBean
    private PatronDetailsService patronDetailsService;

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testBorrowBookSuccess() throws Exception {
        BorrowingRecord record = new BorrowingRecord();
        given(borrowingRecordService.borrowBook(anyLong(), anyLong())).willReturn(record);

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isOk());

    }

    @WithMockUser(username="admin", roles={"USER"})
    @Test
    public void testBorrowBookNotFoundBook() throws Exception {
        given(borrowingRecordService.borrowBook(anyLong(), anyLong())).willThrow(new BookNotFoundException("Book not found"));

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testBorrowBookBorrowingNotAvailable() throws Exception {
        given(borrowingRecordService.borrowBook(anyLong(), anyLong())).willThrow(new BorrowingRecordNotAvailable("Book is already borrowed"));

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book is already borrowed"));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testReturnBookSuccess() throws Exception {
        BorrowingRecord record = new BorrowingRecord(); // Setup return record
        given(borrowingRecordService.returnBook(anyLong(), anyLong())).willReturn(record);

        mockMvc.perform(put("/api/return/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testReturnBookFail() throws Exception {
        given(borrowingRecordService.returnBook(anyLong(), anyLong())).willThrow(new BorrowingRecordNotAvailable("This book was not borrowed by this patron or has already been returned"));

        mockMvc.perform(put("/api/return/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This book was not borrowed by this patron or has already been returned"));
    }
}