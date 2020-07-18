package movieland.web.controllers.api;

import movieland.domain.models.service.SeatServiceModel;
import movieland.domain.models.view.projection.SeatViewModel;
import movieland.services.interfaces.ProjectionsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projections")
public class ProjectionsApiController {

    private final ProjectionsService projectionsService;

    private final ModelMapper modelMapper;

    @Autowired
    public ProjectionsApiController(ProjectionsService projectionsService, ModelMapper modelMapper) {
        this.projectionsService = projectionsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<List<SeatViewModel>> getAllProjectionSeats(@PathVariable String id) {
        Set<SeatServiceModel> allSeatsByProjection = projectionsService.findAllSeatsByProjectionId(id);

        List<SeatViewModel> seatViewModels = allSeatsByProjection.stream()
                .map(seatServiceModel -> modelMapper.map(seatServiceModel, SeatViewModel.class))
                .collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(seatViewModels);
    }

    @PostMapping("/seats/booking")
    public ResponseEntity<List<SeatViewModel>> bookSeatsForProjection(/*@RequestBody*/) {
        return ResponseEntity.ok(new ArrayList<>());
    }
}
