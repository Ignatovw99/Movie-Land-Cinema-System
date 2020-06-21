package movieland.services.implementations;

import movieland.constants.entities.CinemaConstants;
import movieland.domain.entities.Cinema;
import movieland.domain.entities.Hall;
import movieland.domain.models.service.HallServiceModel;
import movieland.errors.duplicate.HallAlreadyExistsException;
import movieland.errors.invalid.HallCinemaNotChangeableException;
import movieland.errors.invalid.InvalidHallException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.errors.notfound.HallNotFoundException;
import movieland.repositories.CinemasRepository;
import movieland.repositories.HallsRepository;
import movieland.services.interfaces.HallsService;
import movieland.services.validation.HallsValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static movieland.constants.entities.HallConstants.*;

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

    private boolean hasCinemaAlreadyTheGivenHall(String hallName, Cinema cinema) {
        return cinemasRepository.existsByIdAndHallsName(cinema.getId(), hallName);
    }

    private boolean isCinemaChanged(Cinema cinema, String newAssignedCinemaId) {
        return !cinema.getId().equals(newAssignedCinemaId);
    }

    private boolean isHallNameChanged(String oldHallName, String newHallName) {
        return !oldHallName.equals(newHallName);
    }

    @Override
    public HallServiceModel create(HallServiceModel hallServiceModel) {
        if (!hallsValidationService.isValid(hallServiceModel)) {
            throw new InvalidHallException(INVALID_HALL_MODEL);
        }

        Cinema hallCinema = cinemasRepository.findById(hallServiceModel.getCinema().getId())
                .orElseThrow(() -> new CinemaNotFoundException(CinemaConstants.CINEMA_NOT_FOUND));

        if (hasCinemaAlreadyTheGivenHall(hallServiceModel.getName(), hallCinema)) {
            throw new HallAlreadyExistsException(HALL_ALREADY_EXISTS_IN_THE_GIVEN_CINEMA);
        }

        Hall hall = modelMapper.map(hallServiceModel, Hall.class);
        hall.setCinema(hallCinema);

        hall = hallsRepository.save(hall);
        return modelMapper.map(hall, HallServiceModel.class);
    }

    @Override
    public HallServiceModel update(String id, HallServiceModel hallServiceModel) {
        if (!hallsValidationService.isValid(hallServiceModel)) {
            throw new InvalidHallException(INVALID_HALL_MODEL);
        }

        Hall hallToUpdate = hallsRepository.findById(id)
                .orElseThrow(() -> new HallNotFoundException(HALL_NOT_FOUND));

        if (isCinemaChanged(hallToUpdate.getCinema(), hallServiceModel.getCinema().getId())) {
            throw new HallCinemaNotChangeableException(HALL_CAN_NOT_CHANGE_ITS_CINEMA);
        }

        if (isHallNameChanged(hallToUpdate.getName(), hallServiceModel.getName()) && hasCinemaAlreadyTheGivenHall(hallServiceModel.getName(), hallToUpdate.getCinema())) {
            throw new HallAlreadyExistsException(HALL_ALREADY_EXISTS_IN_THE_GIVEN_CINEMA);
        }

        modelMapper.map(hallServiceModel, hallToUpdate);
        Hall updatedHall = hallsRepository.save(hallToUpdate);
        return modelMapper.map(updatedHall, HallServiceModel.class);
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
