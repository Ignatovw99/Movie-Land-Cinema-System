package movieland.services.validation.implementations;

import movieland.domain.models.service.HallServiceModel;
import movieland.services.validation.HallsValidationService;
import org.springframework.stereotype.Service;

@Service
public class HallsValidationServiceImpl implements HallsValidationService {

    @Override
    public boolean isValid(HallServiceModel serviceModel) {
        return true;
    }
}
