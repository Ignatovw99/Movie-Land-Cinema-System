package movieland.services.validation.implementations;

import movieland.domain.models.service.GenreServiceModel;
import movieland.services.validation.GenresValidationService;
import org.springframework.stereotype.Service;

@Service
public class GenresValidationServiceImpl implements GenresValidationService {

    private boolean isNameValid(String name) {
        return name != null;
    }

    private boolean isAgeRestrictionValid(Integer ageRestriction) {
        return ageRestriction != null;
    }

    @Override
    public boolean isValid(GenreServiceModel genreServiceModel) {
        return isNameValid(genreServiceModel.getName())
                && isAgeRestrictionValid(genreServiceModel.getAgeRestriction());
    }
}
