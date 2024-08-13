package rmap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "uuser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Column(columnDefinition = "datetime(3)", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User(String email, String password) {
        this(email, password, null);
    }

    public User(String email, String password, LocalDateTime createdAt) {
        this.email = email;
        this.password = encryptPassword(password);
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    private String encryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean matchesPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, password);
    }
}
