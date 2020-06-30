package movieland.repositories;

import movieland.domain.entities.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgrammesRepository extends JpaRepository<Programme, String> {

    Optional<Programme> findFirstByCinemaIdOrderByStartDateDesc(String cinemaId);
}
