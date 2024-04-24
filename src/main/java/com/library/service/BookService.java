package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.BookViolateConstraintException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.repository.BorrowingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    BorrowingRecordRepository borrowingRecordRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    public Book saveBook(Book book) {
        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new BookViolateConstraintException("Failed to create the book due to integrity constraints.", e);
        }
    }

    public Book updateBook(Long id, Book bookDetails) {

        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found");
        }
        try {
            Book book = findBookById(id);
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublicationYear(bookDetails.getPublicationYear());
            book.setIsbn(bookDetails.getIsbn());
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new BookViolateConstraintException("Failed to update the book due to integrity constraints.", e);
        }

    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found");
        }
        borrowingRecordRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }
}
