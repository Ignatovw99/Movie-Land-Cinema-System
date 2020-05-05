package movieland.domain.models.binding.cinema;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

import static movieland.constants.entities.CinemaConstants.TIME_24_HOUR_FORMAT_PATTERN;

public class CinemaUpdateBindingModel {

    private String id;

    private String name;

    private String address;

    private String phoneNumber;

    private String email;

    private LocalTime openingTime;

    private LocalTime closingTime;

    public CinemaUpdateBindingModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @DateTimeFormat(pattern = TIME_24_HOUR_FORMAT_PATTERN)
    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    @DateTimeFormat(pattern = TIME_24_HOUR_FORMAT_PATTERN)
    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
