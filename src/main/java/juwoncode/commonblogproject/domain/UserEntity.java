package juwoncode.commonblogproject.domain;

import jakarta.persistence.*;
import juwoncode.commonblogproject.vo.RoleType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "USER")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", length = 20, unique = true, nullable = false)
    private String username;

    @Column(name = "NICKNAME", length = 16, unique = true, nullable = false)
    private String nickname;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(name = "ACTIVATED")
    @ColumnDefault("false")
    private boolean activated;

    @Column(name = "PROFILE_IMAGE_URL")
    private String profileImageUrl;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "SOCIAL_INFORMATION_ID")
    private SocialInformationEntity socialInformationEntity;

    @Builder
    public UserEntity(String username, String nickname, String password, String email, RoleType role, String profileImageUrl, boolean activated) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.activated = activated;
    }
}
