package Data;

import java.util.ArrayList;

public class Agenda {
    private ArrayList<Artist> artistList;
    private ArrayList<Podium> podiumList;
    private ArrayList<Performance> performanceList;

    public Agenda(ArrayList<Artist> artistList, ArrayList<Podium> podiumList, ArrayList<Performance> performanceList) {
        this.artistList = artistList;
        this.podiumList = podiumList;
        this.performanceList = performanceList;
    }

    public ArrayList<Artist> getArtistList() {
        return artistList;
    }

    public ArrayList<Podium> getPodiumList() {
        return podiumList;
    }

    public ArrayList<Performance> getPerformanceList() {
        return performanceList;
    }

    public void addPodium(Podium podium) {
        this.podiumList.add(podium);
    }

    public void addArtist(Artist artist) {
        this.artistList.add(artist);
    }

    public void removePodium(Podium podiump) {
        this.podiumList.remove(podiump);
    }

    public void removeArtist(Artist artist) {
        this.artistList.remove(artist);
    }
    public void removePerformance(Performance performance){
        this.performanceList.remove(performance);
    }

    @Override
    public String toString() {
        return "Agenda{" +
                "artistList=" + artistList +
                ", podiumList=" + podiumList +
                ", performanceList=" + performanceList +
                '}';
    }
}
