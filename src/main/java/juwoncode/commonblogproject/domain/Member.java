package juwoncode.commonblogproject.domain;

import jakarta.persistence.*;
import juwoncode.commonblogproject.vo.RoleType;
import juwoncode.commonblogproject.vo.SocialType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @ColumnDefault("false")
    private boolean enabled;

    @Column
    private String profileUrl;

    @Builder
    public Member(String username, String password, String email, RoleType role, String socialId, SocialType socialType
            , String profileUrl, boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.socialId = socialId;
        this.socialType = socialType;
        this.profileUrl = profileUrl;
        this.enabled = enabled;
    }
}
