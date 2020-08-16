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

import java.math.BigDecimal;
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

    private boolean checkIsBlocked(boolean stateOfEmergency, int column) {
        return stateOfEmergency && column % 2 == 0;
    }

    private BigDecimal calculatePrice(int row, int col, int numberOfColumns) {
        int multiplyFactor;
        if (col == numberOfColumns / 2 + 1) {
            multiplyFactor = numberOfColumns;
        } else {
            multiplyFactor = numberOfColumns - Math.abs(col - (numberOfColumns / 2 + 1));
        }

        return ProjectionConstants.START_PRICE
                .add(BigDecimal.valueOf(row))
                .multiply(BigDecimal.valueOf(multiplyFactor));
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
        boolean stateOfEmergency = projection.getIsStateOfEmergency();

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= columns; col++) {
                boolean isSeatBlocked = checkIsBlocked(stateOfEmergency, col);

                Seat seat = new Seat()
                        .row(row)
                        .column(col)
                        .isFree(!isSeatBlocked) // When seat is blocked , it should not be free
                        .isBlocked(isSeatBlocked)
                        .price(calculatePrice(row, col, columns))
                        .projection(projection);
                seats.add(seat);
            }
        }

        seatsRepository.saveAll(seats);
    }
}
