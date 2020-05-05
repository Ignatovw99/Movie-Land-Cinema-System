package movieland.domain.models.view.cinema;

import movieland.domain.models.view.BaseViewModel;

public class CinemaViewModel extends BaseViewModel {

    private String name;

    private String address;

    private String phoneNumber;

    private String email;

    private String openingTime;

    private String closingTime;

    private Integer numberOfHalls;

    public CinemaViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public Integer getNumberOfHalls() {
        return numberOfHalls;
    }

    public void setNumberOfHalls(Integer numberOfHalls) {
        this.numberOfHalls = numberOfHalls;
    }
}
