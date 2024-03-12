package juwoncode.commonblogproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenRequest {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
}
