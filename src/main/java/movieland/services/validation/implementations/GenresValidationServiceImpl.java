package movieland.services.validation.implementations;

import movieland.constants.entities.GenreConstants;
import movieland.domain.entities.enumerations.Classification;
import movieland.domain.models.service.GenreServiceModel;
import movieland.services.validation.GenresValidationService;
import org.springframework.stereotype.Service;

@Service
public class GenresValidationServiceImpl implements GenresValidationService {

    private boolean isNameLengthInRange(String name) {
        return name.length() >= GenreConstants.NAME_MIN_LENGTH && name.length() <= GenreConstants.NAME_MAX_LENGTH;
    }

    private boolean isNameValid(String name) {
        return name != null && isNameLengthInRange(name.trim());
    }

    private boolean isClassificationValid(Classification classification) {
        return classification != null;
    }

    private boolean isAgeRestrictionRequiredValid(Boolean isAgeRestrictionRequired) {
        return isAgeRestrictionRequired != null;
    }

    @Override
    public boolean isValid(GenreServiceModel genreServiceModel) {
        return isNameValid(genreServiceModel.getName())
                && isClassificationValid(genreServiceModel.getClassification())
                && isAgeRestrictionRequiredValid(genreServiceModel.getIsAgeRestrictionRequired());
    }
}
