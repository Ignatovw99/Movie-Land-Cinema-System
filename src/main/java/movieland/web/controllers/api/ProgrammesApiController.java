package movieland.web.controllers.api;

import movieland.domain.models.view.programme.CinemaProgrammeDateViewModel;
import movieland.services.interfaces.ProgrammesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/programmes")
public class ProgrammesApiController {

    private final ProgrammesService programmesService;

    @Autowired
    public ProgrammesApiController(ProgrammesService programmesService) {
        this.programmesService = programmesService;
    }

    @GetMapping("/start-date/cinema/{id}")
    public ResponseEntity<LocalDate> getFirstAvailableStartDateForCinema(@PathVariable("id") String cinemaId) {
        LocalDate programmePossibleStartDate = programmesService.getFirstPossibleStartDateForCinema(cinemaId);
        return ResponseEntity.ok(programmePossibleStartDate);
    }

    @GetMapping("/cinema/{id}/date/{date}")
    public ResponseEntity<CinemaProgrammeDateViewModel> getMoviesWithAllTheirProjectionsByCinemaAndDate(@PathVariable("id") String cinemaId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Map<LocalDate, CinemaProgrammeDateViewModel> cinemaProgrammeProjectionsByDate = programmesService.getCurrantActiveCinemaProgrammeWithItsProjections(cinemaId);
        CinemaProgrammeDateViewModel cinemaProgrammeDateViewModel = cinemaProgrammeProjectionsByDate.get(date);
        if (cinemaProgrammeDateViewModel == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cinemaProgrammeDateViewModel);
    }
}
