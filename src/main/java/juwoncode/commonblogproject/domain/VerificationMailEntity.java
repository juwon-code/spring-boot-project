package juwoncode.commonblogproject.domain;

import jakarta.persistence.*;
import juwoncode.commonblogproject.vo.VerificationMailType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "email")
@Getter
@Setter
@NoArgsConstructor
public class VerificationMailEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE", length = 64, nullable = false)
    String code;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private VerificationMailType type;

    @Column(name = "EXPIRED")
    @ColumnDefault("false")
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity userEntity;

    @Builder
    public VerificationMailEntity(String code, VerificationMailType type, UserEntity userEntity) {
        this.code = code;
        this.type = type;
        this.userEntity = userEntity;
    }
}
