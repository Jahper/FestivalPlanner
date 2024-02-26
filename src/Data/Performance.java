package Data;

import java.util.ArrayList;

public class Performance {
    private Podium podium;
    private int startTime;
    private String startTimeGui;
    private int endTime;
    private String endTimeGui;
    private ArrayList<Artist> artists = new ArrayList<>();
    private int popularity;
    private String artistName;

    public Performance(Podium podium, String startTimeHour, String startTimeMinute, String endTimeHour, String endTimeMinute, ArrayList<Artist> artists, int popularity) {
        this.podium = podium;
        this.startTime = Integer.parseInt(startTimeHour + startTimeMinute);
        this.endTime = Integer.parseInt(endTimeHour + endTimeMinute);
        this.popularity = popularity;
        this.startTimeGui = startTimeHour + ":" + startTimeMinute;
        this.endTimeGui = endTimeHour + ":" + endTimeMinute;

        for (Artist artist : artists) {
            this.artists.add(artist);
        }
    }

    public Performance(Podium podium, String startTime, String endTime, ArrayList<Artist> artists, int popularity) {
        this.podium = podium;
        this.popularity = popularity;
        this.startTimeGui = startTime;
        this.endTimeGui = endTime;

        if (startTime.length() > 4) {
            this.startTime = Integer.parseInt(startTime.substring(0,2) + startTime.substring(3));
        } else {
            this.startTime = Integer.parseInt(startTime.substring(0,1) + startTime.substring(2));
        }

        if (endTime.length() > 4) {
            this.endTime = Integer.parseInt(endTime.substring(0,2) + endTime.substring(3));
        } else {
            this.endTime = Integer.parseInt(endTime.substring(0,1) + endTime.substring(2));
        }

        this.artistName = "";
        for (Artist artist : artists) {
            this.artists.add(artist);
            this.artistName = this.artistName + artist.getName() + ", ";
        }
    }

    public Performance(Podium podium, String startTimeHour, String startTimeMinute, String endTimeHour, String endTimeMinute, Artist artist, int popularity) {
        this.podium = podium;
        this.startTime = Integer.parseInt(startTimeHour + startTimeMinute);
        this.endTime = Integer.parseInt(endTimeHour + endTimeMinute);
        this.artists.add(artist);
        this.popularity = popularity;
        this.startTimeGui = startTimeHour + ":" + startTimeMinute;
        this.endTimeGui = endTimeHour + ":" + endTimeMinute;
        this.artistName = artist.getName();
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

    public void setStartTime(String startTimeHour, String startTimeMinute) {
        this.startTime = Integer.parseInt(startTimeHour + startTimeMinute);
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTimeHour, String endTimeMinute) {
        this.endTime = Integer.parseInt(endTimeHour + endTimeMinute);
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

    public String getStartTimeGui() {
        return startTimeGui;
    }

    public String getEndTimeGui() {
        return endTimeGui;
    }

    public String getArtistName() {
        return artistName;
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
