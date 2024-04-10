package rmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false, updatable = false)
    private User user;

    @Column(length = 150, nullable = false)
    private String email;

    @Column(length = 30, nullable = false)
    private String password;

    @CreatedDate
    @Column(columnDefinition = "datetime(3)", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserAccount(User user, String email, String password) {
        this(user, email, password, null);
    }

    public UserAccount(User user, String email, String password, LocalDateTime createdAt) {
        this.user = user;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }
}
