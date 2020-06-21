package movieland.web.controllers.api;

import movieland.domain.models.rest.GenreResponseModel;
import movieland.errors.notfound.GenreNotFoundException;
import movieland.services.interfaces.GenresService;
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
    public ResponseEntity<Set<GenreResponseModel>> allGenresNames() {
        Set<GenreResponseModel> allGenres = genresService
                .findAll()
                .stream()
                .map(genreServiceModel -> modelMapper.map(genreServiceModel, GenreResponseModel.class))
                .collect(Collectors.toUnmodifiableSet());
        return ResponseEntity.ok(allGenres);
    }

    @GetMapping("/is-age-restriction-required/{id}")
    public ResponseEntity<Boolean> checkIfGenreRequiresAgeRestriction(@PathVariable String id) {
        Boolean isAgeRestrictionRequired;
        try {
            isAgeRestrictionRequired = genresService.findById(id).getIsAgeRestrictionRequired();
        } catch (GenreNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(isAgeRestrictionRequired);
    }
}
