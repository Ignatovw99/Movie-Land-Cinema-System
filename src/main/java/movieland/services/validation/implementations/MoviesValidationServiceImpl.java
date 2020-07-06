package movieland.services.validation.implementations;

import movieland.domain.models.service.GenreServiceModel;
import movieland.domain.models.service.MovieServiceModel;
import movieland.services.validation.MoviesValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

import static movieland.constants.entities.MovieConstants.*;

@Service
public class MoviesValidationServiceImpl implements MoviesValidationService {

    private boolean isTitleValid(String title) {
        return title != null
                && title.trim().length() != 0
                && title.length() >= TITLE_MIN_LENGTH
                && title.length() <= TITLE_MAX_LENGTH;
    }

    private boolean isDescriptionValid(String description) {
        return description != null
                && description.trim().length() != 0
                && description.length() >= DESCRIPTION_MIN_LENGTH
                && description.length() <= DESCRIPTION_MAX_LENGTH;
    }

    private boolean isDirectorValid(String director) {
        return director != null
                && director.trim().length() != 0
                && director.length() >= DIRECTOR_MIN_LENGTH
                && director.length() <= DIRECTOR_MAX_LENGTH;
    }

    private boolean isCastValid(Set<String> cast) {
        return cast != null && !cast.isEmpty();
    }

    private boolean isRunningTimeValid(Integer runningTime) {
        return runningTime != null
                && runningTime >= RUNNING_TIME_MIN
                && runningTime <= RUNNING_TIME_MAX;
    }

    private boolean isYearOfProductionValid(Integer yearOfProduction) {
        return yearOfProduction != null
                && yearOfProduction.compareTo(LocalDate.now().getYear()) <= 0;
    }

    private boolean isGenreValid(GenreServiceModel genreServiceModel) {
        return genreServiceModel != null;
    }

    private boolean isAgeRestrictionValid(Boolean isAgeRestrictionRequired, Integer ageRestriction) {
        boolean isAgeRestrictionValid;

        if (isAgeRestrictionRequired == null || !isAgeRestrictionRequired) {
            isAgeRestrictionValid = true;
        } else {
            isAgeRestrictionValid = ageRestriction != null
                    && ageRestriction >= AGE_RESTRICTION_MIN
                    && ageRestriction <= AGE_RESTRICTION_MAX;
        }

        return isAgeRestrictionValid;
    }

    @Override
    public boolean isValid(MovieServiceModel movieServiceModel) {
        return isTitleValid(movieServiceModel.getTitle())
                && isDescriptionValid(movieServiceModel.getDescription())
                && isDirectorValid(movieServiceModel.getDirector())
                && isCastValid(movieServiceModel.getCast())
                && isRunningTimeValid(movieServiceModel.getRunningTime())
                && isYearOfProductionValid(movieServiceModel.getYearOfProduction())
                && isGenreValid(movieServiceModel.getGenre())
                && isAgeRestrictionValid(movieServiceModel.getGenre().getIsAgeRestrictionRequired(), movieServiceModel.getAgeRestriction());
    }
}
