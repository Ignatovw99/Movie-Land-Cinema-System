package movieland.domain.entities;

import movieland.domain.entities.base.BaseEntity;
import movieland.domain.entities.interfaces.Nameable;
import movieland.validation.annotations.email.Email;
import movieland.validation.annotations.phonenumber.PhoneNumber;

import javax.persistence.*;
import java.time.LocalTime;

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
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "address", nullable = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "phone_number", nullable = false)
    @PhoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "email", nullable = false)
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "opening_time", nullable = false)
    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    @Column(name = "closing_time", nullable = false)
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
