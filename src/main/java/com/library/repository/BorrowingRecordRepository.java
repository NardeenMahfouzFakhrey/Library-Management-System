package com.library.repository;

import com.library.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    BorrowingRecord findByBookIdAndPatronId(Long bookId, Long patronId);
    void deleteByBookId(Long bookId);
    void deleteByPatronId(Long patronId);
}