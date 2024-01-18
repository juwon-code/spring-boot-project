package juwoncode.commonblogproject.domain;

import jakarta.persistence.*;
import juwoncode.commonblogproject.vo.EmailType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "email")
@Getter
@Setter
@NoArgsConstructor
public class Email extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(length = 20, nullable = false)
    String code;

    @Enumerated(EnumType.STRING)
    private EmailType type;

    @ColumnDefault("false")
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "m_id", referencedColumnName = "id")
    private Member member;

    @Builder
    public Email(String code, EmailType type, Member member) {
        this.code = code;
        this.type = type;
        this.member = member;
    }
}
