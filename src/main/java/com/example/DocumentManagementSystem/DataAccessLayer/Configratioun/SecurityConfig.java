    package com.example.DocumentManagementSystem.DataAccessLayer.Configratioun;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    public class SecurityConfig {

    @Bean
         SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf->csrf.ignoringRequestMatchers("/public/**")).authorizeHttpRequests((requests) -> requests
                            .requestMatchers("/public/**").permitAll()
                            .anyRequest().permitAll()

                    )


                    .headers(headers->headers.frameOptions(frame->frame.disable()));
            return http.build();
        }
        @Bean
        public PasswordEncoder passwordEncoder()
        {
            return new BCryptPasswordEncoder();
        }

    }
