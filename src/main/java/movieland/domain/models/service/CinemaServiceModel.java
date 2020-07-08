package movieland.domain.models.service;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalTime;
import java.util.Set;

public class CinemaServiceModel extends BaseServiceModel {

    private String name;

    private String address;

    private String phoneNumber;

    private String email;

    private LocalTime openingTime;

    private LocalTime closingTime;

    @JsonBackReference
    private Set<HallServiceModel> halls;

    public CinemaServiceModel() {
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

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public Set<HallServiceModel> getHalls() {
        return halls;
    }

    public void setHalls(Set<HallServiceModel> halls) {
        this.halls = halls;
    }
}
