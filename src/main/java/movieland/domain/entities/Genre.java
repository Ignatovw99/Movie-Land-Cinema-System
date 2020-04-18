package movieland.domain.entities;

import movieland.domain.entities.base.BaseEntity;
import movieland.domain.entities.enumerations.Classification;
import movieland.domain.entities.interfaces.Nameable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static movieland.constants.entities.GenreConstants.*;

@Entity
@Table(name = "genres")
public class Genre extends BaseEntity implements Nameable {

    private String name;

    private Classification classification;

    private Boolean isAgeRestrictionRequired;

    public Genre() {
    }

    @Override
    @Column(name = "name", nullable = false, unique = true, length = 25)
    @NotNull(message = NAME_NOT_EMPTY)
    @Length(min = 3, max = 25, message = NAME_CHARACTERS_LENGTH)
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "classification", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = CLASSIFICATION_NOT_NULL)
    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    @Column(name = "is_age_restriction_required", nullable = false)
    @NotNull(message = IS_AGE_RESTRICTION_REQUIRED_NOT_NULL)
    public Boolean getIsAgeRestrictionRequired() {
        return isAgeRestrictionRequired;
    }

    public void setIsAgeRestrictionRequired(Boolean isAgeRestrictionRequired) {
        this.isAgeRestrictionRequired = isAgeRestrictionRequired;
    }
}
