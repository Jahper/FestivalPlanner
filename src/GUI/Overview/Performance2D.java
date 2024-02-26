package GUI.Overview;

import Data.Artist;
import Data.Performance;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

public class Performance2D {
    private Performance performance;
    private Shape shape;
    private int maxLength;
    private int x;
    private int y;
    private String artists;
    private String timeDuration;
    private String popularity;

    public Performance2D(Performance performance, Shape shape, int maxLength, int x, int y) {
        this.performance = performance;
        this.shape = shape;
        this.maxLength = (maxLength / 10);
        System.out.println(this.maxLength);
        this.x = x;
        this.y = y;
        createArea();
    }

    private void createArea(){
        this.artists = trimString(getArtistString());
        this.timeDuration = trimString(getBeginAndEndTime());
        this.popularity = trimString("Popularity: " + performance.getPopularity());
    }

    private String getBeginAndEndTime() {
        return performance.getStartTimeGui() + " - " + performance.getEndTimeGui();
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
            System.out.println(s);
            return s.substring(0, maxLength) + "...";
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

    public Shape getShape() {
        return shape;
    }

    public Performance getPerformance() {
        return performance;
    }
}
