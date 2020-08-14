package movieland.validation.movie;

import movieland.constants.entities.GenreConstants;
import movieland.domain.entities.Genre;
import movieland.domain.entities.Movie;
import movieland.domain.models.binding.movie.MovieUpdateBindingModel;
import movieland.repositories.GenresRepository;
import movieland.repositories.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Optional;

import static movieland.constants.ValidationErrorCodes.*;
import static movieland.constants.entities.MovieConstants.*;

@movieland.validation.Validator
public class MoviesUpdateValidator implements Validator {

    private final MoviesRepository moviesRepository;

    private final GenresRepository genresRepository;

    @Autowired
    public MoviesUpdateValidator(MoviesRepository moviesRepository, GenresRepository genresRepository) {
        this.moviesRepository = moviesRepository;
        this.genresRepository = genresRepository;
    }

    @Override
    public boolean supports(Class<?> modelClass) {
        return MovieUpdateBindingModel.class.equals(modelClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MovieUpdateBindingModel movieUpdateBindingModel = (MovieUpdateBindingModel) o;

        Genre genreToAssign = null;

        if (movieUpdateBindingModel.getGenreId() != null) {
            genreToAssign = genresRepository.findById(movieUpdateBindingModel.getGenreId())
                    .orElse(null);
        }

        if (movieUpdateBindingModel.getTitle() == null || movieUpdateBindingModel.getTitle().trim().length() == 0) {
            errors.rejectValue(TITLE_FIELD, NULL_ERROR_VALUE, TITLE_NOT_EMPTY);
        } else if (movieUpdateBindingModel.getTitle().trim().length() < TITLE_MIN_LENGTH || movieUpdateBindingModel.getTitle().trim().length() > TITLE_MAX_LENGTH) {
            errors.rejectValue(TITLE_FIELD, INVALID_LENGTH_ERROR, TITLE_CHARACTERS_LENGTH);
        } else if (moviesRepository.existsByTitle(movieUpdateBindingModel.getTitle())) {
            Optional<Movie> movieToUpdate = moviesRepository.findById(movieUpdateBindingModel.getId());
            if (movieToUpdate.isPresent()) {
                if (!movieToUpdate.get().getTitle().equals(movieUpdateBindingModel.getTitle())) {
                    errors.rejectValue(TITLE_FIELD, ALREADY_EXISTS_ERROR, MOVIE_ALREADY_EXISTS);
                }
            }
        }

        if (movieUpdateBindingModel.getDescription() == null || movieUpdateBindingModel.getDescription().trim().length() == 0) {
            errors.rejectValue(DESCRIPTION_FIELD, NULL_ERROR_VALUE, DESCRIPTION_NOT_EMPTY);
        } else if (movieUpdateBindingModel.getDescription().trim().length() < DESCRIPTION_MIN_LENGTH || movieUpdateBindingModel.getDescription().trim().length() > DESCRIPTION_MAX_LENGTH) {
            errors.rejectValue(DESCRIPTION_FIELD, INVALID_LENGTH_ERROR, DESCRIPTION_CHARACTERS_LENGTH);
        }

        if (movieUpdateBindingModel.getDirector() == null || movieUpdateBindingModel.getDirector().trim().length() == 0) {
            errors.rejectValue(DIRECTOR_FIELD, NULL_ERROR_VALUE, DIRECTOR_NOT_NULL);
        } else if (movieUpdateBindingModel.getDirector().trim().length() < DIRECTOR_MIN_LENGTH || movieUpdateBindingModel.getDirector().trim().length() > DIRECTOR_MAX_LENGTH) {
            errors.rejectValue(DIRECTOR_FIELD, INVALID_LENGTH_ERROR, DIRECTOR_CHARACTERS_LENGTH);
        }

        if (movieUpdateBindingModel.getCast() == null || movieUpdateBindingModel.getCast().trim().length() == 0) {
            errors.rejectValue(CAST_FIELD, NULL_ERROR_VALUE, CAST_NOT_NULL);
        }

        if (movieUpdateBindingModel.getRunningTime() == null) {
            errors.rejectValue(RUNNING_TIME_FIELD, NULL_ERROR_VALUE, RUNNING_TIME_NOT_NULL);
        } else if (movieUpdateBindingModel.getRunningTime() < RUNNING_TIME_MIN || movieUpdateBindingModel.getRunningTime() > RUNNING_TIME_MAX) {
            errors.rejectValue(RUNNING_TIME_FIELD, RANGE_ERROR, RUNNING_TIME_RANGE);
        }

        LocalDate todayDate = LocalDate.now();
        Integer currentYear = todayDate.getYear();

        if (movieUpdateBindingModel.getYearOfProduction() == null) {
            errors.rejectValue(YEAR_OF_PRODUCTION_FIELD, NULL_ERROR_VALUE, YEAR_OF_PRODUCTION_NOT_NULL);
        } else if (movieUpdateBindingModel.getYearOfProduction().compareTo(currentYear) > 0) {
            errors.rejectValue(YEAR_OF_PRODUCTION_FIELD, INVALID_VALUE, String.format(YEAR_OF_PRODUCTION_MAX_VALUE, currentYear));
        }

        if (movieUpdateBindingModel.getGenreId() == null) {
            errors.rejectValue(GENRE_CREATE_BINDING_MODEL_FIELD, NULL_ERROR_VALUE, GENRE_NOT_NULL);
        } else if (genreToAssign == null) {
            errors.rejectValue(GENRE_CREATE_BINDING_MODEL_FIELD, NOT_FOUND_ERROR, GenreConstants.GENRE_NOT_FOUND);
        }

        if (genreToAssign != null && genreToAssign.getIsAgeRestrictionRequired() && movieUpdateBindingModel.getAgeRestriction() == null) {
            errors.rejectValue(AGE_RESTRICTION_FIELD, NULL_ERROR_VALUE, AGE_RESTRICTION_NOT_NULL);
        } else if (genreToAssign != null && genreToAssign.getIsAgeRestrictionRequired() && (movieUpdateBindingModel.getAgeRestriction() < AGE_RESTRICTION_MIN || movieUpdateBindingModel.getAgeRestriction() > AGE_RESTRICTION_MAX)) {
            errors.rejectValue(AGE_RESTRICTION_FIELD, RANGE_ERROR, AGE_RESTRICTION_RANGE);
        }
    }
}
