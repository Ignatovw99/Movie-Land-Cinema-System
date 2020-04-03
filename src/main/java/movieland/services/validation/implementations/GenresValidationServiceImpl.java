package movieland.services.validation.implementations;

import movieland.domain.models.service.GenreServiceModel;
import movieland.services.validation.GenresValidationService;
import org.springframework.stereotype.Service;

@Service
public class GenresValidationServiceImpl implements GenresValidationService {

    private boolean isNameValid(String name) {
        return name != null;
    }

    @Override
    public boolean isValid(GenreServiceModel genreServiceModel) {
        return isNameValid(genreServiceModel.getName());
    }
}
