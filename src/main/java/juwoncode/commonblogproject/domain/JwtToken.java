package juwoncode.commonblogproject.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "jwt_token")
@Getter
@Setter
@NoArgsConstructor
public class JwtToken extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @ColumnDefault("false")
    private boolean expired;

    @Builder
    public JwtToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
