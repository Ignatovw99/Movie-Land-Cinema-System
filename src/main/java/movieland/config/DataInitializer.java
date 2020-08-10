package movieland.config;

import movieland.services.interfaces.ProgrammesService;
import movieland.services.interfaces.UsersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final ProgrammesService programmesService;

    private final UsersService usersService;

    public DataInitializer(ProgrammesService programmesService, UsersService usersService) {
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
