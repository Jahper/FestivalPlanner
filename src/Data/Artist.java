package Data;

public class Artist {
    private String name;
    private String genre;

    private String nameGui;

    public Artist(String name, String genre) {
        this.name = name;
        this.genre = genre;
        this.nameGui = name;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public void setName(String name) {
        this.name = name;
        this.nameGui = name;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getNameGui() {
        return this.nameGui;
    }

    @Override
    public String toString() {
        return name;
    }
}