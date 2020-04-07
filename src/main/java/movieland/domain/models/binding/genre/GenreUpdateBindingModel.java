package movieland.domain.models.binding.genre;

import movieland.domain.entities.enumerations.Classification;

public class GenreUpdateBindingModel {

    private String id;

    private String name;

    private Classification classification;

    private Boolean isAgeRestrictionRequired;

    private final Classification[] availableClassifications = Classification.values();

    public GenreUpdateBindingModel() {
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

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Boolean getIsAgeRestrictionRequired() {
        return isAgeRestrictionRequired;
    }

    public void setIsAgeRestrictionRequired(Boolean isAgeRestrictionRequired) {
        this.isAgeRestrictionRequired = isAgeRestrictionRequired;
    }

    public Classification[] getAvailableClassifications() {
        return availableClassifications;
    }
}
