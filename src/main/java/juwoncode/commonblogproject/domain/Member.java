package juwoncode.commonblogproject.domain;

import jakarta.persistence.*;
import juwoncode.commonblogproject.vo.Role;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String email;
    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;

    @Builder
    public Member(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
