package movieland.domain.models.binding;

import movieland.domain.entities.enumerations.Classification;

public class GenreCreateBindingModel {

    private String name;

    private Classification classification;

    private Integer ageRestriction;

    private final Classification[] availableClassifications = Classification.values();

    public GenreCreateBindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Integer getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(Integer ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public Classification[] getAvailableClassifications() {
        return availableClassifications;
    }
}
