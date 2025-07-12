    package com.example.DocumentManagementSystem.DataAccessLayer.Configratioun;

    import com.example.DocumentManagementSystem.DataAccessLayer.Security.JwtAuthenticationFilter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    public class SecurityConfig {
        @Autowired
        private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
         SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf->csrf.ignoringRequestMatchers("/public/**")).authorizeHttpRequests((requests) -> requests
                            .requestMatchers("/public/**").permitAll()
                            .anyRequest().permitAll()

                    )

                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .headers(headers->headers.frameOptions(frame->frame.disable()));
            return http.build();
        }
        @Bean
        public PasswordEncoder passwordEncoder()
        {
            return new BCryptPasswordEncoder();
        }

    }
