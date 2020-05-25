package movieland.web.controllers.api;

import movieland.domain.models.rest.CinemaIdAndNameResponseModel;
import movieland.services.interfaces.CinemasService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/available")
    public ResponseEntity<List<CinemaIdAndNameResponseModel>> getAllAvailableCinemas(@RequestParam String hallName) {
        List<CinemaIdAndNameResponseModel> cinemasResult = cinemasService.findAllCinemasWithoutGivenHallName(hallName)
                .stream()
                .map(cinemaServiceModel -> modelMapper.map(cinemaServiceModel, CinemaIdAndNameResponseModel.class))
                .collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(cinemasResult);
    }
}
