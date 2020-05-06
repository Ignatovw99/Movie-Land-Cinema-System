package movieland.services.implementations;

import movieland.constants.entities.CinemaConstants;
import movieland.domain.entities.Cinema;
import movieland.domain.entities.Hall;
import movieland.domain.models.service.HallServiceModel;
import movieland.errors.duplicate.HallAlreadyExistsException;
import movieland.errors.invalid.InvalidHallException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.repositories.CinemasRepository;
import movieland.repositories.HallsRepository;
import movieland.services.interfaces.HallsService;
import movieland.services.validation.HallsValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static movieland.constants.entities.HallConstants.HALL_ALREADY_EXISTS_IN_THE_GIVEN_CINEMA;
import static movieland.constants.entities.HallConstants.INVALID_HALL_MODEL;

@Service
public class HallsServiceImpl implements HallsService {

    private final HallsRepository hallsRepository;

    private final HallsValidationService hallsValidationService;

    private final CinemasRepository cinemasRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public HallsServiceImpl(HallsRepository hallsRepository, HallsValidationService hallsValidationService, CinemasRepository cinemasRepository, ModelMapper modelMapper) {
        this.hallsRepository = hallsRepository;
        this.hallsValidationService = hallsValidationService;
        this.cinemasRepository = cinemasRepository;
        this.modelMapper = modelMapper;
    }

    private boolean checkIfHallAlreadyExistsInCinema(String hallName, Cinema cinema) {
        return cinemasRepository.existsByIdAndHallsName(cinema.getId(), hallName);
    }

    @Override
    public HallServiceModel create(HallServiceModel hallServiceModel) {
        if (!hallsValidationService.isValid(hallServiceModel)) {
            throw new InvalidHallException(INVALID_HALL_MODEL);
        }

        Cinema hallCinema = cinemasRepository.findById(hallServiceModel.getCinema().getId())
                .orElseThrow(() -> new CinemaNotFoundException(CinemaConstants.CINEMA_NOT_FOUND));

        if (checkIfHallAlreadyExistsInCinema(hallServiceModel.getName(), hallCinema)) {
            throw new HallAlreadyExistsException(HALL_ALREADY_EXISTS_IN_THE_GIVEN_CINEMA);
        }

        Hall hall = modelMapper.map(hallServiceModel, Hall.class);
        hall.setCinema(hallCinema);

        hall = hallsRepository.save(hall);
        return modelMapper.map(hall, HallServiceModel.class);
    }

    @Override
    public HallServiceModel update(String id, HallServiceModel hallServiceModel) {
        return null;
    }

    @Override
    public HallServiceModel delete(String id) {
        return null;
    }

    @Override
    public HallServiceModel findById(String id) {
        return null;
    }

    @Override
    public List<HallServiceModel> findAll() {
        return null;
    }
}
