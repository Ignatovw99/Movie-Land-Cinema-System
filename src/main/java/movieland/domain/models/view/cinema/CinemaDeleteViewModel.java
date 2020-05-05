package movieland.domain.models.view.cinema;

import movieland.domain.models.view.BaseViewModel;

public class CinemaDeleteViewModel extends BaseViewModel {

    private String name;

    public CinemaDeleteViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
