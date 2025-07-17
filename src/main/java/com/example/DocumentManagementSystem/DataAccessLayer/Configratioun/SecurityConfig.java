        package com.example.DocumentManagementSystem.DataAccessLayer.Configratioun;

        import com.example.DocumentManagementSystem.DataAccessLayer.Security.JwtAuthenticationFilter;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.security.config.Customizer;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.http.SessionCreationPolicy;
        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.security.web.SecurityFilterChain;
        import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
        import org.springframework.web.cors.CorsConfiguration;
        import org.springframework.web.cors.CorsConfigurationSource;
        import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

        import java.util.List;


        @Configuration
        public class SecurityConfig {
            @Autowired
            private JwtAuthenticationFilter jwtAuthFilter;

        @Bean
             SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.cors(Customizer.withDefaults()).csrf(csrf->csrf.disable()
                        ).authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/public/**").permitAll()
                                .requestMatchers("/public/me").authenticated()

                                .requestMatchers("/workspace/**").permitAll()
                                .requestMatchers("/document/**").authenticated()
                                .anyRequest().authenticated()

                        )

                        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                        .headers(headers->headers.frameOptions(frame->frame.disable()));
                return http.build();
            }
            @Bean
            public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:4200"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
            }
            @Bean
            public PasswordEncoder passwordEncoder()
            {
                return new BCryptPasswordEncoder();
            }

        }
