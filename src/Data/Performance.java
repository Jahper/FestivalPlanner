package Data;

import GUI.Simulator.NPC.NPC;

import java.util.ArrayList;

public class Performance {
    private Podium podium;
    private int startTime;
    private String startTimeGui;
    private int endTime;
    private String endTimeGui;
    private ArrayList<Artist> artists = new ArrayList<>();
    private ArrayList<NPC> npcList = new ArrayList<>();
    private int popularity;

    public Performance(Podium podium, String startTime, String endTime, ArrayList<Artist> artists, int popularity) {
        this.podium = podium;
        this.popularity = popularity;
        this.startTimeGui = startTime;
        this.endTimeGui = endTime;

        if (startTime.length() > 4) {
            this.startTime = Integer.parseInt(startTime.substring(0, 2) + startTime.substring(3));
        } else {
            this.startTime = Integer.parseInt(startTime.substring(0, 1) + startTime.substring(2));
        }

        if (endTime.length() > 4) {
            this.endTime = Integer.parseInt(endTime.substring(0, 2) + endTime.substring(3));
        } else {
            this.endTime = Integer.parseInt(endTime.substring(0, 1) + endTime.substring(2));
        }

        this.artists.addAll(artists);
    }

    public Performance(Podium podium, String startTimeHour, String startTimeMinute, String endTimeHour, String endTimeMinute, Artist artist, int popularity) {
        this.podium = podium;
        this.startTime = Integer.parseInt(startTimeHour + startTimeMinute);
        this.endTime = Integer.parseInt(endTimeHour + endTimeMinute);
        this.artists.add(artist);
        this.popularity = popularity;
        this.startTimeGui = startTimeHour + ":" + startTimeMinute;
        this.endTimeGui = endTimeHour + ":" + endTimeMinute;
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
        this.startTimeGui = startTimeHour + ":" + startTimeMinute;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTimeHour, String endTimeMinute) {
        this.endTime = Integer.parseInt(endTimeHour + endTimeMinute);
        this.endTimeGui = endTimeHour + ":" + endTimeMinute;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public Artist getArtist() {
        return artists.get(0);
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

    public String getStartTimeHour() {
        return getStartTimeGui().substring(0, 2);
    }

    public String getStartTimeMinute() {
        return getStartTimeGui().substring(3, 5);
    }

    public String getEndTimeHour() {
        return endTimeGui.substring(0, 2);
    }

    public String getEndTimeMinute() {
        return endTimeGui.substring(3, 5);
    }

    public boolean isLive(String hour, String minutes) {
        int time = Integer.parseInt(hour + minutes);
        if (time >= startTime && time <= endTime) {
            return true;
        }

        ArrayList<NPC> tempList = new ArrayList<>();
        for (NPC npc : npcList) {
            npc.setIsBusy(false);
            tempList.add(npc);
        }
        for (NPC npc : tempList) {
            npcList.remove(npc);
        }
        return false;
    }

    public void addNpc(NPC npc) {
        this.npcList.add(npc);
    }

    public int getAttendanceList() {
        ArrayList<NPC> tempList = new ArrayList<>();
        for (NPC npc : npcList) {
            if (npc.isBusy()) {
                tempList.add(npc);
            }
        }
        npcList = tempList;
        return this.npcList.size();
    }

    @Override
    public String toString() {
        return this.getArtist() + " op " + podium + " van " + getStartTimeGui() + " tot " + getEndTimeGui();
    }
}
