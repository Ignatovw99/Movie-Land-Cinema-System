package movieland.domain.entities;

import movieland.domain.entities.base.BaseEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "authorities")
public class UserAuthority extends BaseEntity implements GrantedAuthority {

    private String authority;

    private Boolean isRole;

    public UserAuthority() {
    }

    public UserAuthority(String authority, Boolean isRole) {
        this.authority = authority;
        this.isRole = isRole;
    }


    @Override
    @Column(name = "authority", nullable = false, unique = true)
    @NotNull
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Column(name = "is_role", nullable = false)
    @NotNull
    public Boolean getIsRole() {
        return isRole;
    }

    public void setIsRole(Boolean isRole) {
        this.isRole = isRole;
    }
}
