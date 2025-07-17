package com.example.DocumentManagementSystem.DataAccessLayer.Security;

import com.example.DocumentManagementSystem.BusinessLayer.Services.UserServices;
import com.example.DocumentManagementSystem.DataAccessLayer.JWT.JwtUtil;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.Role;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.Exception.Exceptions.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private final UserServices userServices;
    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserServices userServices) {
        this.jwtUtil = jwtUtil;
        this.userServices = userServices;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtUtil.extractUsername(token);
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                User user = userServices.findByUserName(username);
                if (jwtUtil.isTokenValid(token, user))
                {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null,getGrantedAuthorities(user.getRole()));

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        }
        catch (ExpiredJwtException e)
        {
            throw new TokenExpiredException("Token Expired");
        }
    }
    private List<GrantedAuthority> getGrantedAuthorities (Role role){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        return grantedAuthorities;
    }
}
