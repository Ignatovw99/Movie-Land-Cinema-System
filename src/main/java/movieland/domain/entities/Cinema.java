package movieland.domain.entities;

import movieland.domain.entities.base.BaseEntity;
import movieland.domain.entities.interfaces.Nameable;
import movieland.validation.annotations.email.Email;
import movieland.validation.annotations.phonenumber.PhoneNumber;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

import static movieland.constants.entities.CinemaConstants.*;

@Entity
@Table(name = "cinemas")
public class Cinema extends BaseEntity implements Nameable {

    private String name;

    private String address;

    private String phoneNumber;

    private String email;

    private LocalTime openingTime;

    private LocalTime closingTime;

//    private Set<Hall> halls;

    public Cinema() {
    }

    @Column(name = "name", nullable = false, unique = true)
    @NotNull(message = NAME_NOT_EMPTY)
    @Length(min = NAME_LENGTH_MIN_VALUE, message = NAME_CHARACTERS_LENGTH)
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "address", nullable = false)
    @NotNull
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "phone_number", nullable = false)
    @NotNull
    @PhoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "email", nullable = false)
    @NotNull
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "opening_time", nullable = false)
    @NotNull
    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    @Column(name = "closing_time", nullable = false)
    @NotNull
    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

//    @OneToMany(targetEntity = Hall.class, mappedBy = "cinema", cascade = CascadeType.ALL)
//    public Set<Hall> getHalls() {
//        return halls;
//    }
//
//    public void setHalls(Set<Hall> halls) {
//        this.halls = halls;
//    }
}
