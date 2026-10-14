import java.util.*;

class MusicPlaylistManagerAdvanced {
    Scanner sc = new Scanner(System.in);

    String[] title = {"Perfect","Believer","Kesariya","Bulleya","luther","like JENNIE","Kalyani"};
    String[] artist = {"Ed Sheeran","Imagine Dragons","Arijit Singh","Arijit Singh","SZA","Jennie","ARJN, KDS, FIFTY4, and RONN"};
    String[] album = {"Divide","Evolve","Brahmastra","Ae Dil Hai Mushkil","GNX","Ruby","Kalyani"};
    String[] genre = {"Pop","Rock","Classic","Rock","R&B","Hip-Hop","Hip-Hop"};
    int[] duration = {263,204,268,203,234,198,298};
    int[] plays = {10,12,15,7,15,9,12};

    String[] playlist = new String[10];
    int playlistCount = 0, songCount = 7;
    HashMap<String,Integer> playData = new HashMap<>();

    public static void main(String[] args) {
        MusicPlaylistManagerAdvanced obj = new MusicPlaylistManagerAdvanced();
        obj.start();
    }

    void start() {
        for(int i=0;i<songCount;i++) playData.put(title[i],plays[i]);
        int choice;
        do {
            System.out.println("\nMUSIC PLAYLIST MANAGER");
            System.out.println("1. View Songs");
            System.out.println("2. Search Song");
            System.out.println("3. Add Song");
            System.out.println("4. Add to Playlist");
            System.out.println("5. View Playlist");
            System.out.println("6. Play Song");
            System.out.println("7. Listening Stats");
            System.out.println("8. Advanced Stats");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");
            choice=sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1: viewSongs(); break;
                case 2: searchSong(); break;
                case 3: addSong(); break;
                case 4: addToPlaylist(); break;
                case 5: viewPlaylist(); break;
                case 6: playSong(); break;
                case 7: stats(); break;
                case 8: advancedStats(); break;
                case 9: System.out.println("Thank You!"); break;
                default: System.out.println("Invalid choice.");
            }
        } while(choice!=9);
    }

    void viewSongs() {
        System.out.println("SONG LIBRARY");
        for(int i=0;i<songCount;i++) {
            System.out.println(title[i]+" - "+artist[i]+" - "+genre[i]);
            System.out.println("Album: "+album[i]+" | Duration: "+duration[i]+" seconds");
        }
    }

    void searchSong() {
        System.out.print("Enter song name: ");
        String name=sc.nextLine();
        try {
            int p=findSong(name);
            System.out.println("Song found!");
            System.out.println("Artist: "+artist[p]);
            System.out.println("Album: "+album[p]);
            System.out.println("Genre: "+genre[p]);
        } catch(SongNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    int findSong(String name) throws SongNotFoundException {
        for(int i=0;i<songCount;i++)
            if(title[i].equalsIgnoreCase(name)) return i;
        throw new SongNotFoundException("Song not found.");
    }

    void addSong() {
        if(songCount==10) {
            System.out.println("Library is full.");
            return;
        }
        System.out.print("Enter song name: ");
        title[songCount]=sc.nextLine();
        System.out.print("Enter artist: ");
        artist[songCount]=sc.nextLine();
        System.out.print("Enter album: ");
        album[songCount]=sc.nextLine();
        System.out.print("Enter genre: ");
        genre[songCount]=sc.nextLine();
        System.out.print("Enter duration: ");
        duration[songCount]=sc.nextInt();
        sc.nextLine();
        plays[songCount]=0;
        playData.put(title[songCount],0);
        Track t=new Track(title[songCount],artist[songCount],duration[songCount]);
        saveSong(t);
        songCount++;
        System.out.println("Song added!");
    }

    void addToPlaylist() {
        System.out.print("Enter song name: ");
        String name=sc.nextLine();
        for(int i=0;i<songCount;i++) {
            if(title[i].equalsIgnoreCase(name)) {
                playlist[playlistCount++]=title[i];
                System.out.println("Song added to playlist.");
                return;
            }
        }
        System.out.println("Song not found.");
    }

    void viewPlaylist() {
        System.out.println("MY PLAYLIST");
        if(playlistCount==0) System.out.println("Playlist is empty.");
        for(int i=0;i<playlistCount;i++)
            System.out.println((i+1)+". "+playlist[i]);
    }

    void playSong() {
        System.out.print("Enter song name: ");
        String name=sc.nextLine();
        for(int i=0;i<songCount;i++) {
            if(title[i].equalsIgnoreCase(name)) {
                plays[i]++;
                playData.put(title[i],plays[i]);
                System.out.println("Now Playing: "+title[i]);
                MusicItem item=new SongItem(title[i]);
                item.showType();
                return;
            }
        }
        System.out.println("Song not found.");
    }

    void stats() {
        int total=0, most=0, position=0;
        for(int i=0;i<songCount;i++) {
            total+=plays[i];
            if(plays[i]>most) {
                most=plays[i];
                position=i;
            }
        }
        System.out.println("Total Plays: "+total);
        System.out.println("Most Played Song: "+title[position]);
        System.out.println("Plays: "+most);
    }

    void advancedStats() {
    System.out.println("ADVANCED STATS");

    int total = playData.values().stream().mapToInt(x -> x).sum();
    System.out.println("Total Plays using Streams: " + total);

    System.out.println("Songs with 10 or more plays:");
    playData.entrySet().stream()
        .filter(x -> x.getValue() >= 10)
        .forEach(x -> System.out.println(x.getKey() + " - " + x.getValue() + " plays"));
}

    void saveSong(Track t) {
        try {
            java.io.FileWriter file=new java.io.FileWriter("songs.txt",true);
            file.write(t.title()+" - "+t.artist()+" - "+t.duration()+"\n");
            file.close();
        } catch(Exception e) {
            System.out.println("File error.");
        }
    }
}

abstract class MusicItem {
    abstract void showType();
}

class SongItem extends MusicItem {
    String name;
    SongItem(String name) {
        this.name=name;
    }
    void showType() {
        System.out.println("Type: Song");
    }
}

class SongNotFoundException extends Exception {
    SongNotFoundException(String message) {
        super(message);
    }
}

record Track(String title,String artist,int duration) {}
