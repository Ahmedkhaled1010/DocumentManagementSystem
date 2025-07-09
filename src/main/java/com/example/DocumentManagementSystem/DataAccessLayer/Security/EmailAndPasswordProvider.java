package com.example.DocumentManagementSystem.DataAccessLayer.Security;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Role;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmailAndPasswordProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> user = userRepository.findByEmail(email);
        if (user != null && user.get().getUserId() != null && passwordEncoder.matches(password, user.get().getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, null, getGrantedAuthorities(user.get().getRole()));
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
        }

        private List<GrantedAuthority> getGrantedAuthorities (Role role){
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
            return grantedAuthorities;
        }
        @Override
        public boolean supports (Class < ? > authentication) {
            return authentication.equals(UsernamePasswordAuthenticationToken.class);

        }
}
