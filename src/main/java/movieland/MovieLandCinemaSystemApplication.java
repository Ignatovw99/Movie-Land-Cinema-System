package movieland;

import movieland.services.interfaces.ProgrammesService;
import movieland.services.interfaces.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieLandCinemaSystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MovieLandCinemaSystemApplication.class, args);
    }

    private final ProgrammesService programmesService;

    private final UsersService usersService;

    @Autowired
    public MovieLandCinemaSystemApplication(ProgrammesService programmesService, UsersService usersService) {
        this.programmesService = programmesService;
        this.usersService = usersService;
    }

    @Override
    public void run(String... args) throws Exception {
        programmesService.createAnActiveProgrammeForAllCinemasWithInactiveOnes();
        usersService.seedDatabaseWithAuthorities();
        usersService.createRootAdmin();
    }
}
