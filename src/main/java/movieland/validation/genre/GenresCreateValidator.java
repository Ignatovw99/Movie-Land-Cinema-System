package movieland.validation.genre;

import movieland.domain.models.binding.genre.GenreCreateBindingModel;
import movieland.repositories.GenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static movieland.constants.entities.GenreConstants.*;
import static movieland.constants.ValidationErrorCodes.*;

@movieland.validation.Validator
public class GenresCreateValidator implements Validator {

    private final GenresRepository genresRepository;

    @Autowired
    public GenresCreateValidator(GenresRepository genresRepository) {
        this.genresRepository = genresRepository;
    }

    @Override
    public boolean supports(Class<?> modelClass) {
        return GenreCreateBindingModel.class.equals(modelClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        GenreCreateBindingModel genreCreateBindingModel = (GenreCreateBindingModel) o;

        if (genreCreateBindingModel.getName() == null || genreCreateBindingModel.getName().trim().length() == 0) {
            errors.rejectValue(NAME_FIELD, NULL_ERROR_VALUE, NAME_NOT_EMPTY);
        } else if (genreCreateBindingModel.getName().trim().length() < 3 || genreCreateBindingModel.getName().trim().length() > 25) {
            errors.rejectValue(NAME_FIELD, INVALID_LENGTH_ERROR, NAME_CHARACTERS_LENGTH);
        } else if (genresRepository.existsByName(genreCreateBindingModel.getName())) {
            errors.rejectValue(NAME_FIELD, ALREADY_EXISTS_ERROR, ALREADY_EXISTS_WITH_SUCH_NAME);
        }

        if (genreCreateBindingModel.getIsAgeRestrictionRequired() == null) {
            errors.rejectValue(IS_AGE_RESTRICTION_REQUIRED_FIELD, NULL_ERROR_VALUE, IS_AGE_RESTRICTION_REQUIRED_NOT_NULL);
        }

        if (genreCreateBindingModel.getClassification() == null) {
            errors.rejectValue(CLASSIFICATION_FIELD, NULL_ERROR_VALUE, CLASSIFICATION_NOT_NULL);
        }
    }
}
