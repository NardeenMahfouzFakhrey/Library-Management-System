package com.library.security;

import com.library.model.Patron;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PatronDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Patron patron;

    public PatronDetails(Patron patron) {
        this.patron = patron;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return patron.getPassword();
    }

    @Override
    public String getUsername() {
        return patron.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Patron getPatron() {
        return patron;
    }
}
