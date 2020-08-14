package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Projection;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.repositories.ProjectionsRepository;
import movieland.repositories.SeatsRepository;
import movieland.services.interfaces.SeatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class SeatsServiceTest extends TestBase {

    @MockBean
    private SeatsRepository seatsRepository;

    @MockBean
    private ProjectionsRepository projectionsRepository;

    @Autowired
    private SeatsService seatsService;

    private ProjectionServiceModel projectionServiceModel;

    private Projection projection;

    @Override
    protected void before() {
        projectionServiceModel = ProjectionsServiceTest.initializeServiceModel();
        projection = ProjectionsServiceTest.initializeEntity();

        when(projectionsRepository.findById(anyString()))
                .thenReturn(Optional.of(projection));
    }

    //TODO: Research how to tests @Async methods!!!!!

//    @Test
//    public void generateProjectionSeats_WhenProjectionModelIsNull_ProjectionShouldNotHaveAnySeats() {
//        projectionServiceModel = null;
//
//        seatsService.generateProjectionSeats(projectionServiceModel);
//
//        assertNull(projection.getSeats());
//    }
//
//    @Test
//    public void generateProjectionSeats_WhenProjectionDoesNotExist_ShouldThrowException() {
//        when(projectionsRepository.findById(anyString()))
//                .thenReturn(Optional.empty());
//
//        assertDoesNotThrow(
//                () -> seatsService.generateProjectionSeats(projectionServiceModel)
//        );
//
//        verify(projectionsRepository).findById(anyString());
//    }
}
