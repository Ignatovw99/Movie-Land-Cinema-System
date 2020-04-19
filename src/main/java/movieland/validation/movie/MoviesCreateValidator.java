package movieland.validation.movie;

import movieland.constants.entities.GenreConstants;
import movieland.domain.entities.Genre;
import movieland.domain.models.binding.movie.MovieCreateBindingModel;
import movieland.repositories.GenresRepository;
import movieland.repositories.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static movieland.constants.ValidationErrorCodes.*;
import static movieland.constants.entities.MovieConstants.*;

@movieland.validation.Validator
public class MoviesCreateValidator implements Validator {

    private final MoviesRepository moviesRepository;

    private final GenresRepository genresRepository;

    @Autowired
    public MoviesCreateValidator(MoviesRepository moviesRepository, GenresRepository genresRepository) {
        this.moviesRepository = moviesRepository;
        this.genresRepository = genresRepository;
    }

    @Override
    public boolean supports(Class<?> modelClass) {
        return MovieCreateBindingModel.class.equals(modelClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MovieCreateBindingModel movieCreateBindingModel = (MovieCreateBindingModel) o;

        Genre genreToAssign = null;

        if (movieCreateBindingModel.getGenreId() != null) {
            genreToAssign = genresRepository.findById(movieCreateBindingModel.getGenreId())
                    .orElse(null);
        }

        if (movieCreateBindingModel.getTitle() == null || movieCreateBindingModel.getTitle().trim().length() == 0) {
            errors.rejectValue(TITLE_FIELD, NULL_ERROR_VALUE, TITLE_NOT_EMPTY);
        } else if (movieCreateBindingModel.getTitle().trim().length() < TITLE_MIN_LENGTH || movieCreateBindingModel.getTitle().trim().length() > TITLE_MAX_LENGTH) {
            errors.rejectValue(TITLE_FIELD, INVALID_LENGTH_ERROR, TITLE_CHARACTERS_LENGTH);
        } else if (moviesRepository.existsByTitle(movieCreateBindingModel.getTitle())) {
            errors.rejectValue(TITLE_FIELD, ALREADY_EXISTS_ERROR, MOVIE_ALREADY_EXISTS);
        }

        if (movieCreateBindingModel.getDescription() == null || movieCreateBindingModel.getDescription().trim().length() == 0) {
            errors.rejectValue(DESCRIPTION_FIELD, NULL_ERROR_VALUE, DESCRIPTION_NOT_EMPTY);
        } else if (movieCreateBindingModel.getDescription().trim().length() < DESCRIPTION_MIN_LENGTH || movieCreateBindingModel.getDescription().trim().length() > DESCRIPTION_MAX_LENGTH) {
            errors.rejectValue(DESCRIPTION_FIELD, INVALID_LENGTH_ERROR, DESCRIPTION_CHARACTERS_LENGTH);
        }

        if (movieCreateBindingModel.getDirector() == null || movieCreateBindingModel.getDirector().trim().length() == 0) {
            errors.rejectValue(DIRECTOR_FIELD, NULL_ERROR_VALUE, DIRECTOR_NOT_NULL);
        } else if (movieCreateBindingModel.getDirector().trim().length() < DIRECTOR_MIN_LENGTH || movieCreateBindingModel.getDirector().trim().length() > DIRECTOR_MAX_LENGTH) {
            errors.rejectValue(DIRECTOR_FIELD, INVALID_LENGTH_ERROR, DIRECTOR_CHARACTERS_LENGTH);
        }

        if (movieCreateBindingModel.getCast() == null || movieCreateBindingModel.getCast().trim().length() == 0) {
            errors.rejectValue(CAST_FIELD, NULL_ERROR_VALUE, CAST_NOT_NULL);
        }

        if (movieCreateBindingModel.getRunningTime() == null) {
            errors.rejectValue(RUNNING_TIME_FIELD, NULL_ERROR_VALUE, DIRECTOR_NOT_NULL);
        } else if (movieCreateBindingModel.getRunningTime() < RUNNING_TIME_MIN || movieCreateBindingModel.getRunningTime() > RUNNING_TIME_MAX) {
            errors.rejectValue(RUNNING_TIME_FIELD, RANGE_ERROR, RUNNING_TIME_RANGE);
        }

        LocalDate todayDate = LocalDate.now();
        Integer currentYear = todayDate.getYear();

        if (movieCreateBindingModel.getYearOfProduction() == null) {
            errors.rejectValue(YEAR_OF_PRODUCTION_FIELD, NULL_ERROR_VALUE, YEAR_OF_PRODUCTION_NOT_NULL);
        } else if (movieCreateBindingModel.getYearOfProduction().compareTo(currentYear) > 0) {
            errors.rejectValue(YEAR_OF_PRODUCTION_FIELD, INVALID_VALUE, String.format(YEAR_OF_PRODUCTION_MAX_VALUE, currentYear));
        }

        if (movieCreateBindingModel.getReleaseDate() == null) {
            errors.rejectValue(RELEASE_DATE_FIELD, NULL_ERROR_VALUE, RELEASE_DATE_NOT_NULL);
        } else if (movieCreateBindingModel.getReleaseDate().compareTo(todayDate) <= 0) {
            errors.rejectValue(RELEASE_DATE_FIELD, INVALID_VALUE, String.format(RELEASE_DATE_INVALID_VALUE, todayDate.plusDays(1).format(DateTimeFormatter.ofPattern(RELEASE_DATE_FORMAT))));
        }

        if (movieCreateBindingModel.getGenreId() == null) {
            errors.rejectValue(GENRE_CREATE_BINDING_MODEL_FIELD, NULL_ERROR_VALUE, GENRE_NOT_NULL);
        } else if (genreToAssign == null) {
            errors.rejectValue(GENRE_CREATE_BINDING_MODEL_FIELD, NOT_FOUND_ERROR, GenreConstants.GENRE_NOT_FOUND);
        }

        if (genreToAssign != null && genreToAssign.getIsAgeRestrictionRequired() && movieCreateBindingModel.getAgeRestriction() == null) {
            errors.rejectValue(AGE_RESTRICTION_FIELD, NULL_ERROR_VALUE, AGE_RESTRICTION_NOT_NULL);
        } else if (genreToAssign != null && genreToAssign.getIsAgeRestrictionRequired() && (movieCreateBindingModel.getAgeRestriction() < AGE_RESTRICTION_MIN || movieCreateBindingModel.getAgeRestriction() > AGE_RESTRICTION_MAX)) {
            errors.rejectValue(AGE_RESTRICTION_FIELD, RANGE_ERROR, AGE_RESTRICTION_RANGE);
        }
    }
}
