package movieland.domain.models.view.user;

import movieland.config.mappings.CustomMappable;
import movieland.domain.entities.UserAuthority;
import movieland.domain.models.service.UserServiceModel;
import movieland.domain.models.view.BaseViewModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.Set;

public class UserViewModel extends BaseViewModel implements CustomMappable {

    private String email;

    private String fullName;

    private String role;

    public UserViewModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void configureMappings(ModelMapper modelMapper) {
        Converter<Set<UserAuthority>, String> userAuthorityToRoleAsStringConverter = mappingContext ->{
            if (mappingContext == null) {
                return null;
            }
            String role = null;
            for (UserAuthority userAuthority : mappingContext.getSource()) {
                if (userAuthority.getIsRole()) {
                    role = userAuthority.getAuthority();
                    break;
                }
            }
            return role;
        };

        modelMapper.createTypeMap(UserServiceModel.class, UserViewModel.class)
                .addMappings(map -> map.using(userAuthorityToRoleAsStringConverter).map(
                        UserServiceModel::getAuthorities,
                        UserViewModel::setRole
                ));
    }
}
