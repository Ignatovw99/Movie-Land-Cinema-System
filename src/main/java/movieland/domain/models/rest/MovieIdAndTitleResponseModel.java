package movieland.domain.models.rest;

public class MovieIdAndTitleResponseModel {

    private String id;

    private String title;

    public MovieIdAndTitleResponseModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
