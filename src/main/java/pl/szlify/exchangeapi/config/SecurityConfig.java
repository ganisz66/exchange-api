package pl.szlify.exchangeapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import pl.szlify.exchangeapi.properties.SecurityApiProperties;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/currencies/symbols").permitAll()
                        .requestMatchers("/api/v1/currencies/timeseries").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService users(SecurityApiProperties securityApiProperties) {
        return Optional.ofNullable(securityApiProperties.getUsers())
                .map(users -> {
                    List<UserDetails> userDetailsList = users.stream()
                            .map(user -> User.builder()
                                    .username(user.getUsername())
                                    .password(user.getPassword())
                                    .roles(Optional.ofNullable(user.getRoles())
                                            .map(roles -> roles.split(","))
                                            .orElse(new String[0]))
                                    .build())
                            .collect(Collectors.toList());

                    return new InMemoryUserDetailsManager(userDetailsList);
                })
                .orElse(new InMemoryUserDetailsManager());
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
