package movieland.domain.models.view.movie;

import movieland.domain.models.view.BaseViewModel;

public class MovieDeleteViewModel extends BaseViewModel {

    private String title;

    public MovieDeleteViewModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
