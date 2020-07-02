package movieland.repositories;

import movieland.domain.entities.Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionsRepository extends JpaRepository<Projection, String> {

}
