package movieland.domain.models.service;

import movieland.config.mappings.CustomMappable;
import movieland.domain.entities.User;
import movieland.domain.entities.UserAuthority;
import org.modelmapper.ModelMapper;

import java.util.Set;

public class UserServiceModel extends BaseServiceModel implements CustomMappable {

    private String email;

    private String password;

    private String fullName;

    private Set<UserAuthority> authorities;

    public UserServiceModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<UserAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public void configureMappings(ModelMapper modelMapper) {
        modelMapper.createTypeMap(UserServiceModel.class, User.class)
                .addMappings(map -> map.map(
                        UserServiceModel::getEmail,
                        User::setUsername
                ));
        modelMapper.createTypeMap(User.class, UserServiceModel.class)
                .addMappings(map -> map.map(
                        User::getUsername,
                        UserServiceModel::setEmail
                ));
    }
}
