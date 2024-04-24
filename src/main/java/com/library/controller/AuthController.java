package com.library.controller;

import com.library.exception.PatronViolateConstraintException;
import com.library.model.Patron;
import com.library.security.JwtUtils;
import com.library.service.PatronDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PatronDetailsService patronDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Patron patron) {
        try{
        Patron newPatron = patronDetailsService.registerPatron(patron);
        return ResponseEntity.ok(newPatron); }
        catch (PatronViolateConstraintException e) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            errorDetails.put("cause", e.getCause() != null ? e.getCause().getMessage() : "No additional cause information");
            return ResponseEntity.badRequest().body(errorDetails);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Patron loginRequest) {
        if(loginRequest.getEmail()==null||loginRequest.getPassword()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username and password is required");
        }
        try{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        return ResponseEntity.ok(jwt);  }
        catch(UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login failed: Username not found");
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: Invalid username or password");
        }
    }
}
