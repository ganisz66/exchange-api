package pl.szlify.exchangeapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.szlify.exchangeapi.service.MyUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyUserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/currencies/symbols").permitAll()
                        .requestMatchers("/api/v1/currencies/convert").authenticated()
                        .requestMatchers("/api/v1/currencies/timeseries").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());
        return http.build();
    }

//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{noop}user")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{noop}admin")
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}
