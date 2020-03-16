package movieland.domain.entities;

import movieland.domain.entities.base.BaseEntity;
import movieland.domain.entities.enumerations.Classification;
import movieland.domain.entities.interfaces.Nameable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "genres")
public class Genre extends BaseEntity implements Nameable {

    private String name;

    private Classification classification;

    private Integer ageRestriction;

    public Genre() {
    }

    @Column(name = "name", nullable = false, unique = true)
    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "classification", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    @Column(name = "age_restriction", nullable = false)
    @NotNull
    public Integer getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(Integer ageRestriction) {
        this.ageRestriction = ageRestriction;
    }
}
