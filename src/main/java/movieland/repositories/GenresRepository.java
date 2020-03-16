package movieland.repositories;

import movieland.domain.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenresRepository extends JpaRepository<Genre, String> {

    boolean existsByName(String name);
}
