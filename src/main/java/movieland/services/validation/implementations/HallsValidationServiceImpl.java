package movieland.services.validation.implementations;

import movieland.constants.entities.HallConstants;
import movieland.domain.models.service.HallServiceModel;
import movieland.services.validation.HallsValidationService;
import org.springframework.stereotype.Service;

@Service
public class HallsValidationServiceImpl implements HallsValidationService {

    private boolean isNameValid(String name) {
        return name != null && name.trim().length() >= HallConstants.NAME_LENGTH_MIN_VALUE;
    }

    private boolean isNumberOfRowsValid(Integer rows) {
        return rows != null && rows > 0;
    }

    private boolean isNumberOfColumnsValid(Integer columns) {
        return columns != null && columns > 0;
    }

    private boolean isFilmTechnologyValid(String filmTechnology) {
        return filmTechnology != null;
    }

    private boolean isSoundSystemValid(String soundSystem) {
        return soundSystem != null;
    }

    @Override
    public boolean isValid(HallServiceModel hallServiceModel) {
        return isNameValid(hallServiceModel.getName())
                && isNumberOfRowsValid(hallServiceModel.getRows())
                &&  isNumberOfColumnsValid(hallServiceModel.getColumns())
                && isFilmTechnologyValid(hallServiceModel.getFilmTechnology())
                && isSoundSystemValid(hallServiceModel.getSoundSystem());
    }
}
