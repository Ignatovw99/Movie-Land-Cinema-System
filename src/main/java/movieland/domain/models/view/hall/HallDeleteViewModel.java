package movieland.domain.models.view.hall;

import movieland.domain.models.view.BaseViewModel;

public class HallDeleteViewModel extends BaseViewModel {

    private String name;

    private String cinemaName;

    public HallDeleteViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }
}
