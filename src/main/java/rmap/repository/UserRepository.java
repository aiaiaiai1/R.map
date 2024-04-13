package rmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmap.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
