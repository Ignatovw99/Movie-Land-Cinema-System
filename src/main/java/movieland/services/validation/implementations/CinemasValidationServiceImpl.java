package movieland.services.validation.implementations;

import movieland.domain.models.service.CinemaServiceModel;
import movieland.services.validation.CinemasValidationService;
import org.springframework.stereotype.Service;

@Service
public class CinemasValidationServiceImpl implements CinemasValidationService {

    @Override
    public boolean isValid(CinemaServiceModel serviceModel) {
        return false;
    }
}
