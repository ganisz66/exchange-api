package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.szlify.exchangeapi.model.User;
import pl.szlify.exchangeapi.model.UserPrincipal;
import pl.szlify.exchangeapi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user);
    }
}
