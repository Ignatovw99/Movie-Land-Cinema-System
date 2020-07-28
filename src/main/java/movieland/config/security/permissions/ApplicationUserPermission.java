package movieland.config.security.permissions;

//Authorities
public enum ApplicationUserPermission {

    SEAT_BOOKING("seat:booking"),
    PROJECTION_WRITE("projection:write"),
    PROGRAMME_READ("programme:read"),
    PROGRAMME_WRITE("programme:write"),
    GENRE_READ("genre:read"),
    MOVIE_WRITE("movie:write"),
    GENRE_WRITE("movie:write"),
    CINEMA_READ("cinema:read"),
    CINEMA_WRITE("cinema:write"),
    HALL_READ("hall:read"),
    HALL_WRITE("hall:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
