package movieland.config.security.permissions;

//Authorities
public enum ApplicationUserPermission {

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
