package movieland.services.implementations;

import movieland.domain.entities.Cinema;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.errors.duplicate.CinemaAlreadyExistsException;
import movieland.errors.invalid.InvalidCinemaException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.repositories.CinemasRepository;
import movieland.services.interfaces.CinemasService;
import movieland.services.validation.CinemasValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static movieland.constants.entities.CinemaConstants.*;

@Service
public class CinemasServiceImpl implements CinemasService {

    private final CinemasRepository cinemasRepository;

    private final CinemasValidationService cinemasValidationService;

    private final ModelMapper modelMapper;

    @Autowired
    public CinemasServiceImpl(CinemasRepository cinemasRepository, CinemasValidationService cinemasValidationService, ModelMapper modelMapper) {
        this.cinemasRepository = cinemasRepository;
        this.cinemasValidationService = cinemasValidationService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CinemaServiceModel create(CinemaServiceModel cinemaServiceModel) {
        if (!cinemasValidationService.isValid(cinemaServiceModel)) {
            throw new InvalidCinemaException(INVALID_CINEMA_MODEL);
        }

        if (cinemasRepository.existsByName(cinemaServiceModel.getName())) {
            throw new CinemaAlreadyExistsException(CINEMA_WITH_SUCH_NAME_EXISTS);
        }

        Cinema cinema = modelMapper.map(cinemaServiceModel, Cinema.class);
        cinema = cinemasRepository.save(cinema);
        return modelMapper.map(cinema, CinemaServiceModel.class);
    }

    @Override
    public CinemaServiceModel update(String id, CinemaServiceModel cinemaServiceModel) {
        if (!cinemasValidationService.isValid(cinemaServiceModel)) {
            throw new InvalidCinemaException(INVALID_CINEMA_MODEL);
        }

        Cinema cinemaToUpdate = cinemasRepository.findById(cinemaServiceModel.getId())
                .orElseThrow(() -> new CinemaNotFoundException(CINEMA_NOT_FOUND));

        if (!cinemaToUpdate.getName().equals(cinemaServiceModel.getName())) {
            if (cinemasRepository.existsByName(cinemaServiceModel.getName())) {
                throw new CinemaAlreadyExistsException(CINEMA_ALREADY_EXISTS);
            }
        }

        cinemaToUpdate = modelMapper.map(cinemaServiceModel, Cinema.class);
        Cinema updatedCinema = cinemasRepository.save(cinemaToUpdate);
        return modelMapper.map(updatedCinema, CinemaServiceModel.class);
    }

    @Override
    public CinemaServiceModel delete(String id) {
        return null;
    }

    @Override
    public CinemaServiceModel findById(String id) {
        return null;
    }

    @Override
    public List<CinemaServiceModel> findAll() {
        return null;
    }

    @Override
    public boolean createCinema(CinemaServiceModel cinemaServiceModel) {
        return false;
    }

    @Override
    public Set<CinemaServiceModel> getAll() {
        return null;
    }

    @Override
    public CinemaServiceModel getCinemaById(String id) {
        return null;
    }
}
