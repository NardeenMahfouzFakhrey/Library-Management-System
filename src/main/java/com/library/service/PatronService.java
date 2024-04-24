package com.library.service;

import com.library.exception.PatronNotFoundException;
import com.library.exception.PatronViolateConstraintException;
import com.library.model.Patron;
import com.library.repository.BorrowingRecordRepository;
import com.library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    BorrowingRecordRepository borrowingRecordRepository;

    public List<Patron> findAllPatrons() {
        return patronRepository.findAll();
    }

    public Patron findPatronById(Long id) {
        return patronRepository.findById(id).orElseThrow(() -> new PatronNotFoundException("Patron not found"));
    }

    public void deletePatron(Long id) {
        if (!patronRepository.existsById(id)) {
            throw new PatronNotFoundException("Patron not found");
        }
        borrowingRecordRepository.deleteByPatronId(id);
        patronRepository.deleteById(id);
    }

    public Patron updatePatron(Long id, Patron patronDetails) {

        if (!patronRepository.existsById(id)) {
            throw new PatronNotFoundException("Patron not found");
        }
        try {
            Patron patron = findPatronById(id);

            patron.setName(patronDetails.getName());
            patron.setContactInfo(patronDetails.getContactInfo());
            patron.setEmail(patronDetails.getEmail());
            patron.setPassword(passwordEncoder.encode(patron.getPassword()));

            return patronRepository.save(patron);
        } catch (DataIntegrityViolationException e) {
            throw new PatronViolateConstraintException("Failed to update the patron due to integrity constraints.", e);
        }
    }
}
