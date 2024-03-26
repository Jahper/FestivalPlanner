package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Agenda {
    private ArrayList<Artist> artistList;
    private ArrayList<Podium> podiumList;
    private ArrayList<Performance> performanceList;
    private final File artistFile;
    private final File podiumFile;
    private final File performanceFile;

    public Agenda() {
        this.artistList = new ArrayList<>();
        this.podiumList = new ArrayList<>();
        this.performanceList = new ArrayList<>();
        this.artistFile = new File("Files/Artist.txt");
        this.podiumFile = new File("Files/Podium.txt");
        this.performanceFile = new File("Files/Performance.txt");
    }

    public void save() {
        String fileName = this.artistFile.getPath();
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(artistList);
            oos.writeObject(performanceList);
            oos.writeObject(podiumList);

            oos.close();
        } catch (IOException e){
            e.printStackTrace();
        }

//        fileName = podiumFile.getPath();
//
//        try (PrintWriter print = new PrintWriter(fileName)) {
//            for (Podium podium : podiumList) {
//                print.println(podium.getName());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        fileName = performanceFile.getPath();
//
//        try (PrintWriter print = new PrintWriter(fileName)) {
//            for (Performance performance : performanceList) {
//                print.print(performance.getPodium().getName());
//                print.print("_");
//                print.print(performance.getStartTimeGui());
//                print.print("_");
//                print.print(performance.getEndTimeGui());
//                print.print("_");
//                for (Artist artist : performance.getArtists()) {
//                    print.print(artist.getName());
//                    print.print("/");
//                }
//                print.print("_");
//                print.println(performance.getPopularity());
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
    }

    public void load() {
        artistList.clear();
        podiumList.clear();
        performanceList.clear();

        try {
            FileInputStream fis = new FileInputStream(this.artistFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            artistList = (ArrayList<Artist>) ois.readObject();
            podiumList = (ArrayList<Podium>) ois.readObject();
            performanceList = (ArrayList<Performance>) ois.readObject();
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }

//        this.podiumList.clear();
//
//        try (Scanner scanner = new Scanner(this.podiumFile)) {
//            while (scanner.hasNext()) {
//                this.podiumList.add(new Podium(scanner.nextLine()));
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//
//        this.performanceList.clear();
//
//        Podium podiumInput = new Podium("Undefined");
//        String startTijd;
//        String endTijd;
//        int populariteit;
//        String[] artists;
//        ArrayList<Artist> list = new ArrayList<>();
//        try (Scanner scanner1 = new Scanner(this.performanceFile)) {
//            while (scanner1.hasNext()) {
//                input = scanner1.nextLine().split("_");
//
//                for (Podium podium : this.podiumList) {
//                    if (podium.getName().equals(input[0])) {
//                        podiumInput = podium;
//                    }
//                }
//
//                startTijd = input[1];
//                endTijd = input[2];
//
//                artists = input[3].split("/");
//
//                for (String artistName : artists) {
//                    for (Artist artist : this.artistList) {
//                        if (artist.getName().equals(artistName)) {
//                            list.add(artist);
//                        }
//                    }
//                }
//
//                populariteit = Integer.parseInt(input[4]);
//
//                this.performanceList.add(new Performance(podiumInput, startTijd, endTijd, list, populariteit));
//                list.clear();
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
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
        if (podiumList.size() < 7) {
            this.podiumList.add(podium);
        }
    }

    public void addArtist(Artist artist) {
        this.artistList.add(artist);
    }

    public void addPerformance(Performance performance) {
        if (checkForOverlap(performance)) {
            this.performanceList.add(performance);
        }
    }

    public void removePodium(Podium podium) {

        if (checkPodiumRemove(podium)) {
            podiumList.remove(podium);
        }
    }

    public void removeArtist(Artist artist) {
        if (checkArtistRemove(artist)) {
            artistList.remove(artist);
        }
    }

    public void removePerformance(Performance performance) {
        performanceList.remove(performance);
    }

    //methode voor het controleren of er geen dubbele boeking is
    public boolean checkForOverlap(Performance performance) {
        if (performance.getStartTime() >= performance.getEndTime()) {
            return false;
        }

        int startTime = performance.getStartTime();
        int endTime = performance.getEndTime();

        for (Performance p : performanceList) {
            int pStartTime = p.getStartTime();
            int pEndTime = p.getEndTime();

            if (endTime >= pStartTime && endTime <= pEndTime || startTime >= pStartTime && startTime <= pEndTime) {
                if (p.getPodium().equals(performance.getPodium()) || p.getArtist().equals(performance.getArtist())) {
                    return false;
                }
            }
        }
        return true;
    }

    //methode voor het controleren of er geen dubbele boeking is voor de setters
    public boolean checkForOverlapSetter(Performance performance, Performance updatedPerformance) {
        if (updatedPerformance.getStartTime() >= updatedPerformance.getEndTime()) {
            return false;
        }

        int startTime = updatedPerformance.getStartTime();
        int endTime = updatedPerformance.getEndTime();

        for (Performance p : performanceList) {
            int pStartTime = p.getStartTime();
            int pEndTime = p.getEndTime();
            if (p.equals(performance)) {
                continue;
            }

            if (endTime >= pStartTime && endTime <= pEndTime || startTime >= pStartTime && startTime <= pEndTime) {
                if (p.getPodium().equals(updatedPerformance.getPodium()) || p.getArtist().equals(updatedPerformance.getArtist())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkPodiumRemove(Podium podium) {
        for (Performance performance : performanceList) {
            if (performance.getPodium().equals(podium)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkArtistRemove(Artist artist) {
        for (Performance performance : performanceList) {
            if (performance.getArtist().equals(artist)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Performance> getLivePerformances(int hour, int minutes) {
        ArrayList<Performance> performances = new ArrayList<>();
        for (Performance performance : this.performanceList) {
            if (performance.isLive(hour, minutes)) {
                performances.add(performance);
            }
        }
        return new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Agenda: " +
                "Artiesten: " + artistList +
                ", Podia: " + podiumList +
                ", Optredens: " + performanceList;
    }
}
