package pl.szlify.exchangeapi.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "spring.security")
@Getter
@Setter
public class SecurityApiProperties {
    private List<UserProperty> users = new ArrayList<>();

    @Data
    public static class UserProperty {
        private String username;
        private String password;
        private String roles;
    }
}
