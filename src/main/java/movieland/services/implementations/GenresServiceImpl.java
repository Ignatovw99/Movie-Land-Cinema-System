package movieland.services.implementations;

import movieland.domain.entities.Genre;
import movieland.domain.models.service.GenreServiceModel;
import movieland.repositories.GenresRepository;
import movieland.services.interfaces.GenresService;
import movieland.services.validation.GenresValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenresServiceImpl implements GenresService {

    private final GenresRepository genresRepository;

    private final GenresValidationService genresValidationService;

    private final ModelMapper modelMapper;

    @Autowired
    public GenresServiceImpl(GenresRepository genresRepository, GenresValidationService genresValidationService, ModelMapper modelMapper) {
        this.genresRepository = genresRepository;
        this.genresValidationService = genresValidationService;
        this.modelMapper = modelMapper;
    }

    @Override
    public GenreServiceModel create(GenreServiceModel genreServiceModel) {
        if (!genresValidationService.isValid(genreServiceModel)) {
            //TODO: throw custom exception
            throw new IllegalArgumentException();
        }
        if (genresRepository.existsByName(genreServiceModel.getName())) {
            //TODO: throw another exception
            throw new IllegalArgumentException();
        }
        Genre genre = modelMapper.map(genreServiceModel, Genre.class);
        genresRepository.save(genre);
        return modelMapper.map(genre, GenreServiceModel.class);
    }

    @Override
    public GenreServiceModel update(GenreServiceModel serviceModel) {
        //TODO: implement me!!!!

        //TODO: Make some validations
        //TODO: Check if the genre with this id exists -> update
        //TODO: check if the genre with updated name already exists
        return null;
    }

    @Override
    public GenreServiceModel delete(String id) {
        //TODO: implement me
        return null;
    }

    @Override
    public GenreServiceModel findById(GenreServiceModel id) {
        //TODO: implement me
        return null;
    }

    @Override
    public List<GenreServiceModel> findAll() {
        List<Genre> allGenres = genresRepository.findAll();
        if (allGenres.isEmpty()) {
            //TODO: throw custom ex
            throw new IllegalArgumentException();
        }
        return allGenres
                .stream()
                .map(genre -> modelMapper.map(genre, GenreServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
