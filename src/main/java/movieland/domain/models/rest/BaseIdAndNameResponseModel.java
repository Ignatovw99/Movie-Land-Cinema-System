package movieland.domain.models.rest;

public class BaseIdAndNameResponseModel {

    private String id;

    private String name;

    protected BaseIdAndNameResponseModel() {
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
}
