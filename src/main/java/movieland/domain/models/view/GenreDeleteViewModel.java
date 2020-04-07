package movieland.domain.models.view;

public class GenreDeleteViewModel extends BaseViewModel {

    private String name;

    private int moviesCount;

    public GenreDeleteViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoviesCount() {
        return moviesCount;
    }

    public void setMoviesCount(int moviesCount) {
        this.moviesCount = moviesCount;
    }
}
