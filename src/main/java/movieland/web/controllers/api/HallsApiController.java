package movieland.web.controllers.api;

import movieland.domain.models.rest.HallIdAndNameResponseModel;
import movieland.services.interfaces.HallsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/halls")
public class HallsApiController {

    private final HallsService hallsService;

    private final ModelMapper modelMapper;

    @Autowired
    public HallsApiController(HallsService hallsService, ModelMapper modelMapper) {
        this.hallsService = hallsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/cinema/{id}")
    public ResponseEntity<Set<HallIdAndNameResponseModel>> getHallsNamesByCinemaId(@PathVariable("id") String cinemaId) {
        Set<HallIdAndNameResponseModel> hallsResult = hallsService.findAllByCinema(cinemaId)
                .stream()
                .map(hallServiceModel -> modelMapper.map(hallServiceModel, HallIdAndNameResponseModel.class))
                .collect(Collectors.toUnmodifiableSet());

        return ResponseEntity.ok(hallsResult);
    }
}
