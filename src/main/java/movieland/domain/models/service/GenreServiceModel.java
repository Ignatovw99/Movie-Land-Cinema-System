package movieland.domain.models.service;

import movieland.domain.entities.enumerations.Classification;

public class GenreServiceModel extends BaseServiceModel {

    private String name;

    private Classification classification;

    private Integer ageRestriction;

    public GenreServiceModel() {
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
}
