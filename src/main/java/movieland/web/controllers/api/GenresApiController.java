package movieland.web.controllers.api;

import movieland.domain.models.rest.GenreIdAndNameResponseModel;
import movieland.services.interfaces.GenresService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/genres")
public class GenresApiController {

    private final GenresService genresService;

    private final ModelMapper modelMapper;

    @Autowired
    public GenresApiController(GenresService genresService, ModelMapper modelMapper) {
        this.genresService = genresService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public Set<GenreIdAndNameResponseModel> allGenresNames() {
        return genresService
                .findAll()
                .stream()
                .map(genreServiceModel -> modelMapper.map(genreServiceModel, GenreIdAndNameResponseModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }
}
