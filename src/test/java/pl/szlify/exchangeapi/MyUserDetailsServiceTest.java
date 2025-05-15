package pl.szlify.exchangeapi;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.szlify.exchangeapi.model.User;
import pl.szlify.exchangeapi.repository.UserRepository;
import pl.szlify.exchangeapi.service.MyUserDetailsService;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UserRepository userRepository;

    Faker faker = new Faker();

    @Test
    void loadUserByUsernameTest_HappyPath_ResultInUserDetailsBeingReturned() {
        //given
        User user = User.builder()
                .id(faker.number().randomDigitNotZero())
                .username(faker.name().username())
                .password("Qaz123456")
                .role("USER")
                .build();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        //when
        UserDetails result = myUserDetailsService.loadUserByUsername(user.getUsername());

        //than
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getUsername(), result.getUsername());

        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void loadUserByUsernameTest_UsernameNotFoundException_ResultInUserNotFoundException() {
        //given
        String username = faker.name().username();
        when(userRepository.findByUsername(username)).thenReturn(null);

        //when//than
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> myUserDetailsService.loadUserByUsername(username))
                .withMessage(username);

        verify(userRepository).findByUsername(username);
    }
}
