package Data;

import java.util.ArrayList;

public class Performance {
    private Podium podium;
    private int startTime;
    private int endTime;
    private ArrayList<Artist> artists;
    private int popularity;

    public Performance(Podium podium, int startTime, int endTime, ArrayList<Artist> artists, int popularity) {
        this.podium = podium;
        this.startTime = startTime;
        this.endTime = endTime;
        this.artists = artists;
        this.popularity = popularity;
    }
    public Performance(Podium podium, int startTime, int endTime, Artist artist, int popularity){
        this.podium = podium;
        this.startTime = startTime;
        this.endTime = endTime;
        this.artists = new ArrayList<>();
        this.artists.add(artist);
        this.popularity = popularity;
    }

    public Podium getPodium() {
        return podium;
    }

    public void setPodium(Podium podium) {
        this.podium = podium;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "Performance{" +
                "podium=" + podium +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", artists=" + artists +
                ", popularity=" + popularity +
                '}';
    }
}
