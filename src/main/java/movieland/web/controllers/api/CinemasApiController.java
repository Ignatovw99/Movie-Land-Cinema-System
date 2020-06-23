package movieland.web.controllers.api;

import movieland.domain.models.rest.CinemaIdAndNameResponseModel;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.services.interfaces.CinemasService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cinemas")
public class CinemasApiController {

    private final CinemasService cinemasService;

    private final ModelMapper modelMapper;

    public CinemasApiController(CinemasService cinemasService, ModelMapper modelMapper) {
        this.cinemasService = cinemasService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CinemaIdAndNameResponseModel>> getAllCinemas() {
        List<CinemaIdAndNameResponseModel> cinemasResult = cinemasService.findAll()
                .stream()
                .map(cinemaServiceModel -> modelMapper.map(cinemaServiceModel, CinemaIdAndNameResponseModel.class))
                .collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(cinemasResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaIdAndNameResponseModel> getCinemaById(@PathVariable String id) {
        CinemaServiceModel cinemaServiceModel = cinemasService.findById(id);
        CinemaIdAndNameResponseModel cinemaResult = modelMapper.map(cinemaServiceModel, CinemaIdAndNameResponseModel.class);
        return ResponseEntity.ok(cinemaResult);
    }

    @GetMapping("/available")
    public ResponseEntity<List<CinemaIdAndNameResponseModel>> getAllAvailableCinemas(@RequestParam String hallName) {
        List<CinemaIdAndNameResponseModel> cinemasResult = cinemasService.findAllCinemasWithoutGivenHallName(hallName)
                .stream()
                .map(cinemaServiceModel -> modelMapper.map(cinemaServiceModel, CinemaIdAndNameResponseModel.class))
                .collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(cinemasResult);
    }

    @GetMapping("/by-hall/{hallId}")
    public ResponseEntity<CinemaIdAndNameResponseModel> getCinemaByHallId(@PathVariable String hallId) {
        CinemaServiceModel cinemaServiceModel = cinemasService.findCinemaByHallId(hallId);
        CinemaIdAndNameResponseModel cinemaResult = modelMapper.map(cinemaServiceModel, CinemaIdAndNameResponseModel.class);
        return ResponseEntity.ok(cinemaResult);
    }
}
