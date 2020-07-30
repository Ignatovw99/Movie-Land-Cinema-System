package movieland.domain.models.binding.user;

public class UserLoginBindingModel {

    private String email;

    private String password;

    private boolean rememberMe;

    private boolean areCredentialsInvalid;

    public UserLoginBindingModel() {
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

    public boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public boolean getAreCredentialsInvalid() {
        return areCredentialsInvalid;
    }

    public void setAreCredentialsInvalid(boolean areCredentialsInvalid) {
        this.areCredentialsInvalid = areCredentialsInvalid;
    }
}
