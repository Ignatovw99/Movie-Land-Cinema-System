package movieland.repositories;

import movieland.domain.entities.User;
import movieland.domain.entities.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String email);

    Optional<User> findByUsername(String email);

    boolean existsByAuthoritiesEquals(UserAuthority authority);
}
