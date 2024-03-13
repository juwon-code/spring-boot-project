package juwoncode.commonblogproject.domain;

import jakarta.persistence.*;
import juwoncode.commonblogproject.vo.SocialPlatformType;
import lombok.*;

@Entity
@Table(name = "SOCIAL_INFORMATION")
@Getter
@Setter
@NoArgsConstructor
public class SocialInformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PLATFORM_ID")
    private String platformId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private SocialPlatformType type;

    @Builder
    public SocialInformationEntity(String platformId, String nickname, SocialPlatformType type) {
        this.platformId = platformId;
        this.nickname = nickname;
        this.type = type;
    }
}
