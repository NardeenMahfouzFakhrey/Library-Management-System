package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.BorrowingRecordNotAvailable;
import com.library.exception.PatronNotFoundException;
import com.library.model.Book;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;
import com.library.repository.BookRepository;
import com.library.repository.BorrowingRecordRepository;
import com.library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class BorrowingRecordService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PatronRepository patronRepository;
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new PatronNotFoundException("Patron not found"));

        if (!book.getAvailable()) {
            throw new BorrowingRecordNotAvailable("Book is already borrowed");
        }
        book.setAvailable(false);
        bookRepository.save(book);

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowDate(new Date());
        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        BorrowingRecord record = borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId);

        if (record != null && record.getReturnDate() == null) {
            Book book = record.getBook();
            book.setAvailable(true);
            bookRepository.save(book);
            record.setReturnDate(new Date());
            return borrowingRecordRepository.save(record);
        } else {
            throw new BorrowingRecordNotAvailable("This book was not borrowed by this patron or has already been returned");
        }
    }
    }