package movieland.web.controllers.api;

import movieland.domain.models.rest.CinemaIdAndNameResponseModel;
import movieland.services.interfaces.CinemasService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
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

    @GetMapping("/names")
    public Set<CinemaIdAndNameResponseModel> allCinemaNames() {
        return this.cinemasService.findAll()
                .stream()
                .map(cinemaServiceModel -> this.modelMapper.map(cinemaServiceModel, CinemaIdAndNameResponseModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }
}
