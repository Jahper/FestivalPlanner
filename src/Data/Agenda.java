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

    public void removePodium(String name) {
        for (Podium podium : this.podiumList) {
            if (podium.getName().equals(name)) {
                this.podiumList.remove(podium);
            }
        }

    }

    public void removeArtist(String name) {
        for (Artist artist : this.artistList) {
            if (artist.getName().equals(name)) {
                this.podiumList.remove(artist);
            }
        }
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
