package com.visa.services.imple;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.visa.modals.User;

@Service
public class UserDetailsImplementation implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String mobileNumber;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImplementation(User user) {
        this.mobileNumber = user.getMobileNumber();
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }

    public UserDetailsImplementation() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // Not required for OTP-based login
    }

    @Override
    public String getUsername() {
        return mobileNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImplementation that = (UserDetailsImplementation) o;
        return mobileNumber.equals(that.mobileNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobileNumber);
    }
}
