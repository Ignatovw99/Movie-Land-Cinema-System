package movieland.repositories;

import movieland.domain.entities.Cinema;
import movieland.domain.entities.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ProgrammesRepository extends JpaRepository<Programme, String> {

    Optional<Programme> findFirstByCinemaIdOrderByStartDateDesc(String cinemaId);

    Optional<Programme> findFirstByCinemaIdOrderByEndDateDesc(String cinemaId);

    void deleteByEndDateBefore(LocalDate endDate);

    @Query("SELECT programme FROM Programme programme WHERE programme.cinema = :cinema AND programme.startDate <= :currentDate AND programme.endDate >= :currentDate")
    Optional<Programme> findProgrammeOfCinemaInGivenPeriod(@Param("cinema") Cinema cinema, @Param("currentDate") LocalDate currentDate);
}
