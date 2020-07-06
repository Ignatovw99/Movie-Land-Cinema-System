package movieland.services.interfaces;

import movieland.domain.models.service.HallServiceModel;
import movieland.services.interfaces.base.CrudService;

import java.util.List;

public interface HallsService extends CrudService<HallServiceModel, String> {

    List<HallServiceModel> findAllByCinema(String cinemaId);
}
