package Data;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Agenda {
    private final ArrayList<Artist> artistList;
    private final ArrayList<Podium> podiumList;
    private final ArrayList<Performance> performanceList;
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

        try (PrintWriter print = new PrintWriter(fileName)) {
            for (Artist artist : this.artistList) {
                print.print(artist.getName());
                print.print("_");
                print.println(artist.getGenre());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileName = podiumFile.getPath();

        try (PrintWriter print = new PrintWriter(fileName)) {
            for (Podium podium : podiumList) {
                print.println(podium.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileName = performanceFile.getPath();

        try (PrintWriter print = new PrintWriter(fileName)) {
            for (Performance performance : performanceList) {
                print.print(performance.getPodium().getName());
                print.print("_");
                print.print(performance.getStartTimeGui());
                print.print("_");
                print.print(performance.getEndTimeGui());
                print.print("_");
                for (Artist artist : performance.getArtists()) {
                    print.print(artist.getName());
                    print.print("/");
                }
                print.print("_");
                print.println(performance.getPopularity());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        artistList.clear();

        String name;
        String genre;
        String[] input;
        try (Scanner scanner = new Scanner(this.artistFile)) {
            while (scanner.hasNext()) {
                input = scanner.nextLine().split("_");
                name = input[0];
                genre = input[1];
                this.artistList.add(new Artist(name, genre));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        this.podiumList.clear();

        try (Scanner scanner = new Scanner(this.podiumFile)) {
            while (scanner.hasNext()) {
                this.podiumList.add(new Podium(scanner.nextLine()));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        this.performanceList.clear();

        Podium podiumInput = new Podium("Undefined");
        String startTijd;
        String endTijd;
        int populariteit;
        String[] artists;
        ArrayList<Artist> list = new ArrayList<>();
        try (Scanner scanner1 = new Scanner(this.performanceFile)) {
            while (scanner1.hasNext()) {
                input = scanner1.nextLine().split("_");

                for (Podium podium : this.podiumList) {
                    if (podium.getName().equals(input[0])) {
                        podiumInput = podium;
                    }
                }

                startTijd = input[1];
                endTijd = input[2];

                artists = input[3].split("/");

                for (String artistName : artists) {
                    for (Artist artist : this.artistList) {
                        if (artist.getName().equals(artistName)) {
                            list.add(artist);
                        }
                    }
                }

                populariteit = Integer.parseInt(input[4]);

                this.performanceList.add(new Performance(podiumInput, startTijd, endTijd, list, populariteit));
                list.clear();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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

    public void addPerformance(Performance performance) {
        if (checkForOverlap(performance)) {
            this.performanceList.add(performance);
        }
    }

    public boolean checkForOverlap(Performance performance) {
        if (performance.getStartTime() >= performance.getEndTime()){
            return false;
        }

        int startTime = performance.getStartTime();
        int endTime = performance.getEndTime();

        for (Performance p : performanceList) {
            int pStartTime = p.getStartTime();
            int pEndTime = p.getEndTime();

            if (endTime > pStartTime && endTime < pEndTime || startTime > pStartTime && startTime < pEndTime) {
                if (p.getPodium().equals(performance.getPodium()) || p.getArtist().equals(performance.getArtist())
                        && p.getPodium().equals(performance.getPodium())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void removePodium(Podium podium) {
        this.podiumList.remove(podium);
    }

    public void removeArtist(Artist artist) {
        this.artistList.remove(artist);
    }

    public void removePerformance(Performance performance) {
        this.performanceList.remove(performance);
    }

    @Override
    public String toString() {
        return "Agenda: " +
                "Artiesten: " + artistList +
                ", Podia: " + podiumList +
                ", Optredens: " + performanceList;
    }
}
