package movieland.services.implementations;

import movieland.domain.entities.Genre;
import movieland.domain.models.service.GenreServiceModel;
import movieland.errors.duplicate.GenreAlreadyExistsException;
import movieland.errors.invalid.InvalidGenreModelException;
import movieland.errors.notfound.GenreNotFoundException;
import movieland.repositories.GenresRepository;
import movieland.services.interfaces.GenresService;
import movieland.services.validation.GenresValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    //TODO: add to constants
    @Override
    public GenreServiceModel create(GenreServiceModel genreServiceModel) {
        if (!genresValidationService.isValid(genreServiceModel)) {
            throw new InvalidGenreModelException("Invalid genre service model");
        }

        if (genresRepository.existsByName(genreServiceModel.getName())) {
            throw new GenreAlreadyExistsException("A genre with such name already exists");
        }

        Genre genre = modelMapper.map(genreServiceModel, Genre.class);
        genre = genresRepository.save(genre);
        return modelMapper.map(genre, GenreServiceModel.class);
    }

    @Override
    public GenreServiceModel update(String id, GenreServiceModel genreServiceModelToUpdate) {
        if (!genresValidationService.isValid(genreServiceModelToUpdate)) {
            throw new InvalidGenreModelException("Invalid genre service model");
        }

        Genre genreToUpdate = genresRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException("Genre with such id does not exist"));

        if (!genreToUpdate.getName().equals(genreServiceModelToUpdate.getName())) {
            if (genresRepository.existsByName(genreServiceModelToUpdate.getName())) {
                throw new GenreAlreadyExistsException("Genre with such name exists already.");
            }
        }

        genreToUpdate = modelMapper.map(genreServiceModelToUpdate, Genre.class);
        Genre updatedGenre = genresRepository.save(genreToUpdate);
        return modelMapper.map(updatedGenre, GenreServiceModel.class);
    }

    @Override
    public GenreServiceModel delete(String id) {
        Genre genreToDelete = genresRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException("Genre with such id does not exist"));

        genresRepository.delete(genreToDelete);

        return modelMapper.map(genreToDelete, GenreServiceModel.class);
    }

    @Override
    public GenreServiceModel findById(String id) {
        Genre foundGenre = genresRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException("Genre with such id does not exist"));

        return modelMapper.map(foundGenre, GenreServiceModel.class);
    }

    @Override
    public List<GenreServiceModel> findAll() {
        return genresRepository.findAll()
                .stream()
                .map(genre -> modelMapper.map(genre, GenreServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
