package movieland.repositories;

import movieland.domain.entities.Projection;
import movieland.domain.entities.projections.ProjectionBookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectionsRepository extends JpaRepository<Projection, String> {

    @Query("SELECT new movieland.domain.entities.projections.ProjectionBookingDetails(m.title, c.name, h.name, p.startingTime, COUNT(s.id), SUM(s.price))" +
            "FROM Projection p " +
            "JOIN Movie m ON m.id = p.movie " +
            "JOIN Hall h ON h.id = p.hall " +
            "JOIN Cinema c ON c.id = h.cinema " +
            "JOIN Seat s ON p.id = s.projection " +
            "JOIN User u ON u.id = s.bookedBy " +
            "WHERE u.username = :username " +
            "GROUP BY p ")
    Set<ProjectionBookingDetails> findAllProjectionBookingsByUser(@Param("username") String username);
}
