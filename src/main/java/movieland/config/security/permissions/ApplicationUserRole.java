package movieland.config.security.permissions;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static movieland.config.security.permissions.ApplicationUserPermission.*;

public enum ApplicationUserRole {

    USER(new HashSet<>(List.of(SEAT_BOOKING))),
    CINEMA_MODERATOR(mergeAuthorities(USER.permissions, new HashSet<>(List.of(PROJECTION_WRITE)))),
    CINEMA_ADMIN(mergeAuthorities(CINEMA_MODERATOR.permissions, new HashSet<>(List.of(PROGRAMME_READ, PROGRAMME_WRITE)))),
    MOVIE_MODERATOR(mergeAuthorities(USER.permissions, new HashSet<>(List.of(GENRE_READ, MOVIE_WRITE)))),
    MOVIE_ADMIN(mergeAuthorities(MOVIE_MODERATOR.permissions, new HashSet<>(List.of(GENRE_WRITE)))),
    ADMIN(mergeAuthorities(mergeAuthorities(CINEMA_ADMIN.permissions, MOVIE_ADMIN.permissions), new HashSet<>(List.of()))),
    ROOT_ADMIN(new HashSet<>());

    private static Set<ApplicationUserPermission> mergeAuthorities(Set<ApplicationUserPermission> oldPermissions, Set<ApplicationUserPermission> newPermissions) {
        return Stream.of(oldPermissions, newPermissions)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
