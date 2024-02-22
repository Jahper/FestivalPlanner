package GUI.Overview;

import Data.Artist;
import Data.Performance;

import java.awt.geom.Area;
import java.util.ArrayList;

public class Performance2D {
    private Performance performance;
    private int x;
    private int y;

    public Performance2D(Performance performance) {
        this.performance = performance;
    }

    private void createArea(){
        String artists = getArtistString();
        String timeDuration = getBeginAndEndTime();
        String popularity = "Popularity: " + performance.getPopularity();










    }

    private String getBeginAndEndTime() {
        return performance.getStartTime() + " - " + performance.getEndTime();
    }

    private String getArtistString() {
        ArrayList<Artist> artists = performance.getArtists();
        StringBuilder artistName;
        if (artists.size() == 1){
            artistName = new StringBuilder("Artist: " + artists.get(0));
        } else {
            artistName = new StringBuilder("Artists: ");
            for (Artist artist : artists) {
                artistName.append(" " + artist.getName() + ",");
                //todo laatste comma weghalen
            }
        }
        return String.valueOf(artistName);
    }
}
