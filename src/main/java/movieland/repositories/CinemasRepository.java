package movieland.repositories;

import movieland.domain.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemasRepository extends JpaRepository<Cinema, String> {

    boolean existsByName(String name);
}
