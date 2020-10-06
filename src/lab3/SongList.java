package lab2;

/**
 *
 * @author Bogdan Kovalov
 * @since 10/5/2020
 * @version 1.0
 *
 * Description: The main point of this program is that it tells you the names of
 * the artists that appear on the top 200 list for Spotify and how many times
 * they appear on that list. The way this program works is that it scans in a
 * file in which this case its a csv file and its the top 200 artists for the
 * week on Spotify. While the scanner is scanning in each number and each string
 * there is multiple for loops in my program that keep track of the number of
 * artists, how many times they appear, and how many unique artists there are in
 * the top 200 list.I also used another technique of implementing a if loop to
 * see if the same artist appears more than one time and if they do there is a
 * counter which keeps track of this. I also declared a PrintStream object in
 * order to print to a file all the artists that were found and how many times
 * they reappeared on the top 200 list.
 *
 */
import java.util.*;
import java.io.*;

public class SongList {

    public static void main(String[] args) throws Exception {
        int row = 109; //the amount of artists excluding repetitions
        int col = 2;
        String[][] StreamList = new String[row][col];
        read(StreamList);

//adds elements from the 2D array into the linked list
        TopStreamingArtists streams = new TopStreamingArtists();
        for (int r = 0; r < row; r++) {
            streams.insertFirst(StreamList[r][0], StreamList[r][1]);
        }
        streams.AscendingSort();
        streams.displayList();
    }

//reads in data from the csv file and fills the 2D array, arr, which refers to StreamList in Main.
    public static void read(String[][] arr) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("regional-global-daily-latest.csv"));
        String note = br.readLine();
        String titles = br.readLine();

        String data = br.readLine();

//splits around commas.
// ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)" is a short-hand for ignoring commas within quotations.
        String[] line = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        arr[0][0] = line[2].trim();
        arr[0][1] = Integer.toString(1);

        int rowCount = 1;
        for (int r = 1; r < arr.length + 1; r++) {
            data = br.readLine();
            line = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (search(arr, line[2].trim(), r) == 0) {
                arr[rowCount][0] = line[2].trim();
                arr[rowCount][1] = Integer.toString(1);
                rowCount++;
            } else {
                int pos = search(arr, line[2].trim(), r);
                int count = Integer.parseInt(arr[pos][1]) + 1;
                String c = Integer.toString(count);
                arr[pos][1] = c;
                r = rowCount;
            }
        }
        br.close();
    }

//searches the array as it is being inputted for other artists of the same name.
//returns 0 if the artist is not found.
//returns the position the artist is found if the artist is there already
    public static int search(String[][] arr, String name, int pos) {
        for (int r = 0; r < pos; r++) {
            if (name.equals(arr[r][0])) {
                return r;
            }
        }
        return 0;
    }
}

//A node represents an artist
class Artist {

    private String name;
    private String appearances;
    public Artist next;

    Artist(String name, String appearances) {
        this.name = name;
        this.appearances = appearances;
    }

    public void setName(String n) {
        name = n;
    }

    public void setAppearances(String a) {
        appearances = a;
    }

    public String getName() {
        return name;
    }

    public String getAppearances() {
        return appearances;
    }

    public String displayArtist() {
        return (name + ", " + appearances);
    }
}

//TopStreamingArtists is a collection of Artist nodes.
class TopStreamingArtists {

    private Artist first;

    public TopStreamingArtists() {
        first = null;
    }

    public boolean isEmpty() {
        return (first == null);
    }

//inserts each element as the first in the linkedlist
    public void insertFirst(String artistName, String numAppear) {
        Artist a = new Artist(artistName, numAppear);
        a.next = first;
        first = a;
    }

//sorts the name in the linkedlist in ascending alphabetical order using Artist names.
//If the comparison between two strings is positive, the first string alphabetically comes after the second string.
    public void AscendingSort() {
        Artist current = first;
        Artist next = null;
        String NameTemp;
        String CountTemp;
        if (first == null) {
            return;
        } else {
            while (current != null) {
                next = current.next;
                while (next != null) {
                    if (current.getName().compareToIgnoreCase(next.getName()) > 0) {
                        NameTemp = current.getName();
                        CountTemp = current.getAppearances();

                        current.setName(next.getName());
                        current.setAppearances(next.getAppearances());

                        next.setName(NameTemp);
                        next.setAppearances(CountTemp);
                    }
                    next = next.next;
                }
                current = current.next;
            }
        }
    }

    public void displayList() throws Exception {
        PrintWriter pw = new PrintWriter("ArtistOutput.txt");
        Artist current = first;
        while (current != null) {
            current.displayArtist();
            pw.println(current.displayArtist());
            current = current.next;
        }
        pw.close();
    }
}
