package movieland.repositories;

import movieland.domain.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemasRepository extends JpaRepository<Cinema, String> {

    boolean existsByName(String name);

    boolean existsByIdAndHallsName(String cinemaId, String hallName);

    @Query("SELECT cinema FROM Cinema cinema WHERE cinema NOT IN (SELECT c FROM Cinema c JOIN c.halls h WHERE h.name = :hallName)")
    List<Cinema> findAllCinemasWithoutGivenHallName(@Param("hallName") String hallName);

    boolean existsByIdAndHallsNameEquals(String cinemaId, String hallName);
}
