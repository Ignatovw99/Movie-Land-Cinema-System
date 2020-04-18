package movieland.web.controllers.api;

import movieland.domain.models.rest.HallIdAndNameResponseModel;
import movieland.services.interfaces.CinemasService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/halls")
public class HallsApiController {

    private final CinemasService cinemasService;

    private final ModelMapper modelMapper;

    @Autowired
    public HallsApiController(CinemasService cinemasService, ModelMapper modelMapper) {
        this.cinemasService = cinemasService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/cinemaId={id}")
    public Set<HallIdAndNameResponseModel> getHallsNamesByCinemaId(@PathVariable("id") String cinemaId) {
        return cinemasService.getCinemaById(cinemaId)
                .getHalls()
                .stream()
                .map(hallServiceModel -> modelMapper.map(hallServiceModel, HallIdAndNameResponseModel.class))
                .collect(Collectors.toUnmodifiableSet());

    }
}
