package movieland.repositories;

import movieland.domain.entities.Projection;
import movieland.domain.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatsRepository extends JpaRepository<Seat, String> {

    boolean existsByProjectionAndIsFree(Projection projection, boolean isFree);
}
