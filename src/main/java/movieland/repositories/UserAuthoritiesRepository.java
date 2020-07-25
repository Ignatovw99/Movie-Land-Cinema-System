package movieland.repositories;

import movieland.domain.entities.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthoritiesRepository extends JpaRepository<UserAuthority, String> {

    boolean existsByAuthority(String authority);

    List<UserAuthority> findAllByIsRoleIsTrue();

    UserAuthority findByAuthority(String authority);
}
