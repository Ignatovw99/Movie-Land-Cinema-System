package movieland.services.implementations;

import movieland.constants.entities.GenreConstants;
import movieland.domain.entities.Genre;
import movieland.domain.entities.Movie;
import movieland.domain.models.service.MovieServiceModel;
import movieland.errors.duplicate.MovieAlreadyExistsException;
import movieland.errors.invalid.InvalidMovieException;
import movieland.errors.notfound.GenreNotFoundException;
import movieland.errors.notfound.MovieNotFoundException;
import movieland.repositories.GenresRepository;
import movieland.repositories.MoviesRepository;
import movieland.services.interfaces.MoviesService;
import movieland.services.validation.MoviesValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static movieland.constants.entities.MovieConstants.*;

@Service
public class MoviesServiceImpl implements MoviesService {

    private final MoviesRepository moviesRepository;

    private final MoviesValidationService moviesValidationService;

    private final GenresRepository genresRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public MoviesServiceImpl(MoviesRepository moviesRepository, MoviesValidationService moviesValidationService, GenresRepository genresRepository, ModelMapper modelMapper) {
        this.moviesRepository = moviesRepository;
        this.moviesValidationService = moviesValidationService;
        this.genresRepository = genresRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MovieServiceModel create(MovieServiceModel movieServiceModel) {
        if (!moviesValidationService.isValid(movieServiceModel)) {
            throw new InvalidMovieException(INVALID_MOVIE_MODEL);
        }

        if (moviesRepository.existsByTitle(movieServiceModel.getTitle())) {
            throw new MovieAlreadyExistsException(MOVIE_ALREADY_EXISTS);
        }

        Genre genre = genresRepository.findById(movieServiceModel.getGenre().getId())
                .orElseThrow(() -> new GenreNotFoundException(GenreConstants.GENRE_NOT_FOUND));

        Movie movie = modelMapper.map(movieServiceModel, Movie.class);
        movie.setGenre(genre);

        movie = moviesRepository.save(movie);

        return modelMapper.map(movie, MovieServiceModel.class);
    }

    @Override
    public MovieServiceModel update(String id, MovieServiceModel movieServiceModel) {
        if (!moviesValidationService.isValid(movieServiceModel)) {
            throw new InvalidMovieException(INVALID_MOVIE_MODEL);
        }

        Movie movieToUpdate = moviesRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(MOVIE_NOT_FOUND));

        if (!movieToUpdate.getTitle().equals(movieServiceModel.getTitle())) {
            if (moviesRepository.existsByTitle(movieServiceModel.getTitle())) {
                throw new MovieAlreadyExistsException(MOVIE_ALREADY_EXISTS);
            }
        }

        if (!movieToUpdate.getGenre().getId().equals(movieServiceModel.getGenre().getId())) {
            Genre newMovieGenre = genresRepository.findById(movieServiceModel.getGenre().getId())
                    .orElseThrow(() -> new GenreNotFoundException(GenreConstants.GENRE_NOT_FOUND));
            movieToUpdate.setGenre(newMovieGenre);
            if (!newMovieGenre.getIsAgeRestrictionRequired()) {
                movieServiceModel.setAgeRestriction(null);
                movieToUpdate.setAgeRestriction(null);
            }
        }

        modelMapper.map(movieServiceModel, movieToUpdate);
        Movie updatedMovie = moviesRepository.save(movieToUpdate);
        return modelMapper.map(updatedMovie, MovieServiceModel.class);
    }

    @Override
    @Transactional
    public MovieServiceModel delete(String id) {
        Movie movieToDelete = moviesRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(MOVIE_WITH_SUCH_ID_NOT_FOUND));

        moviesRepository.delete(movieToDelete);

        return modelMapper.map(movieToDelete, MovieServiceModel.class);
    }

    @Override
    public MovieServiceModel findById(String id) {
        return modelMapper.map(moviesRepository.findById(id).get(), MovieServiceModel.class);
    }

    @Override
    public List<MovieServiceModel> findAll() {
        return moviesRepository.findAll()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Set<MovieServiceModel> getAll() {
        return moviesRepository.findAll()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Optional<MovieServiceModel> getById(String movieId) {
        Optional<Movie> movieCandidate =  moviesRepository.findById(movieId);

        if (movieCandidate.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(modelMapper.map(movieCandidate.get(), MovieServiceModel.class));
    }
}
