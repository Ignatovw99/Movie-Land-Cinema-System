package movieland.services.implementations;

import movieland.constants.entities.ProjectionConstants;
import movieland.domain.entities.Projection;
import movieland.domain.entities.Seat;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.errors.notfound.ProjectionNotFoundException;
import movieland.repositories.ProjectionsRepository;
import movieland.repositories.SeatsRepository;
import movieland.services.interfaces.SeatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeatsServiceImpl implements SeatsService {

    private final SeatsRepository seatsRepository;

    private final ProjectionsRepository projectionsRepository;

    @Autowired
    public SeatsServiceImpl(SeatsRepository seatsRepository, ProjectionsRepository projectionsRepository) {
        this.seatsRepository = seatsRepository;
        this.projectionsRepository = projectionsRepository;
    }

    @Async
    @Override
    public void generateProjectionSeats(ProjectionServiceModel projectionServiceModel) {
        if (projectionServiceModel == null) {
            return;
        }

        Projection projection = projectionsRepository.findById(projectionServiceModel.getId())
                .orElseThrow(() -> new ProjectionNotFoundException(ProjectionConstants.PROJECTION_NOT_FOUND));

        List<Seat> seats = new ArrayList<>();

        Integer rows = projection.getHall().getRows();
        Integer columns = projection.getHall().getColumns();
        boolean stateOfEmergency = projection.isStateOfEmergency();

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= columns; col++) {
                Seat seat = new Seat();
                seat.setRow(row);
                seat.setColumn(col);
                seat.setIsFree(true);
                if (stateOfEmergency && col % 2 == 0) {
                    seat.setIsBlocked(true);
                } else {
                    seat.setIsBlocked(false);
                }
                seat.setProjection(projection);
                seats.add(seat);
            }
        }

        seatsRepository.saveAll(seats);
        System.out.println(LocalDateTime.now());
    }
}
