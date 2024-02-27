package GUI.Overview;

import Data.Artist;
import Data.Performance;

import java.awt.*;
import java.util.ArrayList;

public class Performance2D {
    private final Performance performance;
    private final Shape shape;
    private final int maxLength;
    private final int x;
    private final int y;
    private final int endX;
    private String artists;
    private String timeDuration;
    private String popularity;
    /*
    een klasse voor het opslaan van data uit een performance en dit bij het tekenen kunnen tonen,
    slaat ook coordinates op van de getekende squares
    */
    public Performance2D(Performance performance, Shape shape, int maxLength, int x, int y, int endX) {
        this.performance = performance;
        this.shape = shape;
        this.maxLength = (int)(maxLength / 7.5);
        this.x = x;
        this.y = y;
        this.endX = endX + x;
        createArea();
    }

    private void createArea() {
        this.artists = trimString(getArtistString());
        this.timeDuration = trimString(getBeginAndEndTime());
        this.popularity = trimString("Popularity: " + performance.getPopularity());
    }
    //geeft een String van de begintijd en eindtijd
    private String getBeginAndEndTime() {
        return performance.getStartTimeGui() + " - " + performance.getEndTimeGui();
    }
    //geeft een string van de/alle artiest(en)
    private String getArtistString() {
        ArrayList<Artist> artists = performance.getArtists();
        StringBuilder artistName;
        if (artists.size() == 1) {
            artistName = new StringBuilder("Artist: " + artists.get(0).getName());
        } else {
            artistName = new StringBuilder("Artists: ");
            for (int i = 0; i < artists.size(); i++) {
                if (i == artists.size() - 1){
                    artistName.append(" ").append(artists.get(i).getName());
                } else {
                    artistName.append(" ").append(artists.get(i).getName()).append(",");
                }
            }
        }
        return String.valueOf(artistName);
    }
    //methode om een string in te korten zodat deze niet buiten het vierkant valt
    private String trimString(String s) {
        if (s.length() > maxLength) {
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
    public int getEndX() {
        return endX;
    }
}
