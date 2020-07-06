package movieland.repositories;

import movieland.domain.entities.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallsRepository extends JpaRepository<Hall, String> {

    List<Hall> findAllByCinemaId(String cinemaId);
}
