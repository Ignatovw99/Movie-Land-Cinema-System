package movieland.web.controllers.api;

import movieland.services.interfaces.ProgrammesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/programmes")
public class ProgrammesApiController {

    private ProgrammesService programmesService;

    @Autowired
    public ProgrammesApiController(ProgrammesService programmesService) {
        this.programmesService = programmesService;
    }

    @GetMapping("start-date/cinema/{id}")
    public ResponseEntity<LocalDate> getFirstAvailableStartDateForCinema(@PathVariable("id") String cinemaId) {
        LocalDate programmePossibleStartDate = programmesService.getFirstPossibleStartDateForCinema(cinemaId);
        return ResponseEntity.ok(programmePossibleStartDate);
    }
}
