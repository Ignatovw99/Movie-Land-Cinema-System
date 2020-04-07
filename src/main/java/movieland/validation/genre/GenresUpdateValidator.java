package movieland.validation.genre;

import movieland.domain.entities.Genre;
import movieland.domain.models.binding.genre.GenreUpdateBindingModel;
import movieland.repositories.GenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

import static movieland.constants.ValidationErrorCodes.*;
import static movieland.constants.entities.GenreConstants.*;

@movieland.validation.Validator
public class GenresUpdateValidator implements Validator {

    private final GenresRepository genresRepository;

    @Autowired
    public GenresUpdateValidator(GenresRepository genresRepository) {
        this.genresRepository = genresRepository;
    }

    @Override
    public boolean supports(Class<?> modelClass) {
        return GenreUpdateBindingModel.class.equals(modelClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        GenreUpdateBindingModel genreUpdateBindingModel = (GenreUpdateBindingModel) o;

        if (genreUpdateBindingModel.getName() == null || genreUpdateBindingModel.getName().trim().length() == 0) {
            errors.rejectValue(NAME_FIELD, NULL_ERROR_VALUE, NAME_NOT_EMPTY);
        } else if (genreUpdateBindingModel.getName().length() < 3 || genreUpdateBindingModel.getName().length() > 25) {
            errors.rejectValue(NAME_FIELD, INVALID_LENGTH_ERROR, NAME_CHARACTERS_LENGTH);
        } else if (genresRepository.existsByName(genreUpdateBindingModel.getName())) {
            Optional<Genre> genreToUpdate = genresRepository.findById(genreUpdateBindingModel.getId());
            if (genreToUpdate.isPresent()) {
                if (!genreToUpdate.get().getName().equals(genreUpdateBindingModel.getName())) {
                    errors.rejectValue(NAME_FIELD, ALREADY_EXISTS_ERROR, ALREADY_EXISTS_WITH_SUCH_NAME);
                }
            }
        }

        if (genreUpdateBindingModel.getIsAgeRestrictionRequired() == null) {
            errors.rejectValue(IS_AGE_RESTRICTION_REQUIRED_FIELD, NULL_ERROR_VALUE, IS_AGE_RESTRICTION_REQUIRED_NOT_NULL);
        }

        if (genreUpdateBindingModel.getClassification() == null) {
            errors.rejectValue(CLASSIFICATION_FIELD, NULL_ERROR_VALUE, CLASSIFICATION_NOT_NULL);
        }
    }
}
