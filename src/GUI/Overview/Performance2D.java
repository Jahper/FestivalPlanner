package GUI.Overview;

import Data.Artist;
import Data.Performance;

import java.awt.geom.Area;
import java.util.ArrayList;

public class Performance2D {
    private Performance performance;
    private int maxLength;
    private int x;
    private int y;
    private String artists;
    private String timeDuration;
    private String popularity;

    public Performance2D(Performance performance, int maxLength, int x, int y) {
        this.performance = performance;
        this.maxLength = maxLength;
        this.x = x;
        this.y = y;
        createArea();
    }

    private void createArea(){
        this.artists = getArtistString();
        this.timeDuration = getBeginAndEndTime();
        this.popularity = trimString("Popularity: " + performance.getPopularity());
    }

    private String getBeginAndEndTime() {
        return performance.getStartTime() + " - " + performance.getEndTime();
    }

    private String getArtistString() {
        ArrayList<Artist> artists = performance.getArtists();
        StringBuilder artistName;
        if (artists.size() == 1){
            artistName = new StringBuilder("Artist: " + artists.get(0).getName());
        } else {
            artistName = new StringBuilder("Artists: ");
            for (Artist artist : artists) {
                artistName.append(" " + artist.getName() + ",");
                //todo laatste comma weghalen
            }
        }
        return String.valueOf(artistName);
    }
    private String trimString(String s){
        if (s.length() > maxLength){
            return s.substring(0, maxLength);
        }
        return s;
    }

    public String getArtists() {
        return artists;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public String getPopularity() {
        return popularity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
