package movieland.services.implementations;

import movieland.domain.models.service.CinemaServiceModel;
import movieland.repositories.CinemasRepository;
import movieland.services.interfaces.CinemasService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CinemasServiceImpl implements CinemasService {

    private final CinemasRepository cinemasRepository;

    private final ModelMapper modelMapper;

    public CinemasServiceImpl(CinemasRepository cinemasRepository, ModelMapper modelMapper) {
        this.cinemasRepository = cinemasRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CinemaServiceModel create(CinemaServiceModel serviceModel) {
        return null;
    }

    @Override
    public CinemaServiceModel update(String id, CinemaServiceModel serviceModel) {
        return null;
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
