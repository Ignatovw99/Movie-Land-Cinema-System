package movieland.repositories;

import movieland.domain.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, String> {

    boolean existsByTitle(String title);
}
