package com.library.service;

import com.library.exception.BookViolateConstraintException;
import com.library.exception.PatronNotFoundException;
import com.library.exception.PatronViolateConstraintException;
import com.library.model.Patron;
import com.library.repository.PatronRepository;
import com.library.security.PatronDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PatronDetailsService implements UserDetailsService {

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Patron patron = patronRepository.findByEmail(email);
        if (patron == null) {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }
        return new PatronDetails(patron);
    }

    public Patron registerPatron(Patron patron) {
        try{
        patron.setPassword(passwordEncoder.encode(patron.getPassword()));
        return patronRepository.save(patron); }catch (DataIntegrityViolationException e) {
            throw new PatronViolateConstraintException("Failed to create the book due to integrity constraints.", e);
        }

    }
}
