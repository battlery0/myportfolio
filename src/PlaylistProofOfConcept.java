import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Proof of concept for the Playlist component (Portfolio Part 2).
 *
 * <p>
 * Songs can be sorted into themes: K-pop idols, K-bands, J-pop, EDM, and game
 * soundtracks.
 */
public final class PlaylistProofOfConcept {

    //Themes
    public enum Theme {
        KPOP_IDOLS("K-pop Idols"), K_BANDS("K-Bands"), JPOP("J-pop"), EDM(
                "EDM"), GAME_OST("Game OST");

        private final String label;

        Theme(String label) {
            this.label = label;
        }

        public String label() {
            return this.label;
        }
    }

    public record Song(String title, String artist, int durationSeconds,
            Theme theme) {
        public Song {
            if (title == null || artist == null || theme == null) {
                throw new IllegalArgumentException("Song fields cannot be null");
            }
            if (durationSeconds <= 0) {
                throw new IllegalArgumentException(
                        "Duration must be positive");
            }
        }
    }

    private final Deque<Song> songs = new ArrayDeque<>();

    // Kernel methods

    // Adds {@code s} to the back of this playlist.
    public void addSong(Song s) {
        this.songs.addLast(s);
    }

    // Removes and returns the song at the front of this playlist.
    public Song removeFirst() {
        if (this.songs.isEmpty()) {
            throw new IllegalStateException("Playlist is empty");
        }
        return this.songs.removeFirst();
    }

    // Reports the number of songs in this playlist.
    public int length() {
        return this.songs.size();
    }

    // Secondary methods (written using only the kernel methods above)

    // Moves the front song to the back of this playlist.
    public void skip() {
        if (this.length() > 0) {
            this.addSong(this.removeFirst());
        }
    }

    // Randomly reorders the songs in this playlist.
    public void shuffle(Random rng) {
        List<Song> temp = this.drain();
        Collections.shuffle(temp, rng);
        for (Song s : temp) {
            this.addSong(s);
        }
    }

    // Reverses the order of the songs in this playlist.
    public void reverse() {
        List<Song> temp = this.drain();
        for (int i = temp.size() - 1; i >= 0; i--) {
            this.addSong(temp.get(i));
        }
    }

    // Reports whether a song with the given title exists.
    public boolean containsTitle(String title) {
        boolean found = false;
        for (int i = 0, n = this.length(); i < n; i++) {
            Song s = this.removeFirst();
            if (s.title().equals(title)) {
                found = true;
            }
            this.addSong(s);
        }
        return found;
    }

    // Reports the total duration of all songs in seconds.
    public int totalDuration() {
        int total = 0;
        for (int i = 0, n = this.length(); i < n; i++) {
            Song s = this.removeFirst();
            total += s.durationSeconds();
            this.addSong(s);
        }
        return total;
    }

    //Moves all songs of {@code other} onto the back of this playlist.
    public void appendPlaylist(PlaylistProofOfConcept other) {
        while (other.length() > 0) {
            this.addSong(other.removeFirst());
        }
    }

    //Theme-based secondary methods

    //Reports how many songs in this playlist have the given theme.
    public int countOf(Theme theme) {
        int count = 0;
        for (int i = 0, n = this.length(); i < n; i++) {
            Song s = this.removeFirst();
            if (s.theme() == theme) {
                count++;
            }
            this.addSong(s);
        }
        return count;
    }

    //Reports the total duration (seconds) of songs with the given theme.
    public int durationOf(Theme theme) {
        int total = 0;
        for (int i = 0, n = this.length(); i < n; i++) {
            Song s = this.removeFirst();
            if (s.theme() == theme) {
                total += s.durationSeconds();
            }
            this.addSong(s);
        }
        return total;
    }

    //Removes every song with the given theme from this playlist and returns them (in their original order) as a new playlist.
    public PlaylistProofOfConcept extractTheme(Theme theme) {
        PlaylistProofOfConcept result = new PlaylistProofOfConcept();
        for (int i = 0, n = this.length(); i < n; i++) {
            Song s = this.removeFirst();
            if (s.theme() == theme) {
                result.addSong(s);
            } else {
                this.addSong(s);
            }
        }
        return result;
    }

    // Skips songs until the front song has the given theme (if any does).
    public void skipToTheme(Theme theme) {
        if (this.countOf(theme) > 0) {
            while (this.songs.peekFirst().theme() != theme) {
                this.skip();
            }
        }
    }

    //Removes all songs and returns them in order
    private List<Song> drain() {
        List<Song> temp = new ArrayList<>();
        while (this.length() > 0) {
            temp.add(this.removeFirst());
        }
        return temp;
    }

    private static String formatTime(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }


    public String preview(int max) {
        StringBuilder sb = new StringBuilder();
        int n = this.length();
        for (int i = 0; i < n; i++) {
            Song s = this.removeFirst();
            if (i < max) {
                sb.append(String.format("  %2d. %s - %s [%s, %s]%n", i + 1,
                        s.artist(), s.title(), s.theme().label(),
                        formatTime(s.durationSeconds())));
            }
            this.addSong(s);
        }
        if (n > max) {
            sb.append(String.format("  ... and %d more%n", n - max));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.preview(this.length());
    }

    // Sample data (client)

    private static void add(PlaylistProofOfConcept p, Theme theme,
            String title, String artist, String time) {
        String[] parts = time.split(":");
        int secs = Integer.parseInt(parts[0]) * 60
                + Integer.parseInt(parts[1]);
        p.addSong(new Song(title, artist, secs, theme));
    }

    private static PlaylistProofOfConcept buildMyMix() {
        PlaylistProofOfConcept p = new PlaylistProofOfConcept();

        Theme k = Theme.KPOP_IDOLS;
        add(p, k, "neon", "A2O", "3:43");
        add(p, k, "Attention", "NewJeans", "2:58");
        add(p, k, "404 (New era)", "KiiiKiii", "3:00");
        add(p, k, "LOVE ATTACK", "RESCENE", "3:18");
        add(p, k, "SIGN", "IZNA", "3:10");
        add(p, k, "Whiplash", "aespa", "3:11");
        add(p, k, "Trampoline", "Billie", "3:38");
        add(p, k, "Magnetic", "ILLIT", "3:09");
        add(p, k, "REDRED", "CORTIS", "3:29");
        add(p, k, "BAD", "ATEEZ", "3:45");
        add(p, k, "Fly Up", "RIIZE", "3:08");

        Theme b = Theme.K_BANDS;
        add(p, b, "Surrender", "MRCH", "3:33");
        add(p, b, "The only scent", "MRCH", "3:19");
        add(p, b, "MRCH Mood", "MRCH", "3:00");
        add(p, b, "Tricky", "MRCH", "2:50");
        add(p, b, "Color it", "MRCH", "3:53");
        add(p, b, "Though we have the earth", "MRCH", "3:17");
        add(p, b, "Isn't that good?", "YUDABIN BAND", "3:33");
        add(p, b, "200%", "AKMU", "3:31");
        add(p, b, "Give Love", "AKMU", "3:50");
        add(p, b, "Moon", "AKMU", "4:04");
        add(p, b, "Cistus", "Hyangseong", "3:16");
        add(p, b, "Anti Buzzing Club", "Hyangseong", "3:43");
        add(p, b, "Don't be afraid to fall", "HANRORO", "3:32");
        add(p, b, "Landing in Love", "HANRORO", "2:47");

        Theme j = Theme.JPOP;
        add(p, j, "Koi", "Hoshino Gen", "4:53");
        add(p, j, "kirari", "Fujii Kaze", "4:01");
        add(p, j, "Que sera sera", "Mrs. GREEN APPLE", "4:38");
        add(p, j, "Nanndemonaiyo", "Macaroni Empitsu", "4:31");
        add(p, j, "Chinchoge", "DISH//", "3:30");
        add(p, j, "Neko", "DISH//", "4:36");
        add(p, j, "Dramaturgy", "Eve", "4:06");
        add(p, j, "Fight Song", "Eve", "5:08");
        add(p, j, "ACACIA", "BUMP OF CHICKEN", "4:23");
        add(p, j, "PAKU", "asmi", "2:12");
        add(p, j, "Ikuoku Kounen", "Omoinotake", "5:16");
        add(p, j, "Femme fatale A", "Mulasaki Ima", "3:40");
        add(p, j, "That Classmate", "Mulasaki Ima", "3:19");
        add(p, j, "Turing Love", "Nanawoakari", "3:41");
        add(p, j, "Dadadada Tenshi", "Nanawoakari", "3:39");
        add(p, j, "Hai Yorokonde", "Kocchi no Kento", "2:44");
        add(p, j, "Sugar Song to Bitter Step", "unison", "4:14");

        Theme e = Theme.EDM;
        add(p, e, "Dopamine", "Wings", "2:49");
        add(p, e, "multi_arm", "Frums", "5:06");
        add(p, e, "MIRINAE", "tak", "4:18");
        add(p, e, "Insight", "Haywyre", "3:55");
        add(p, e, "Smooth Criminal", "Haywyre", "4:09");
        add(p, e, "Fantasize", "PurpleJam", "3:51");
        add(p, e, "We Could Get More Machinegun Psystyle!", "Camellia",
                "5:26");
        add(p, e, "GHOST", "Camellia", "5:50");
        add(p, e, "Peer Gynt", "cYsmix", "2:21");

        Theme g = Theme.GAME_OST;
        add(p, g, "It's Playtime", "Poppy Playtime", "0:57");
        add(p, g, "Hope Is the Thing with Feathers", "Honkai: Star Rail", "2:56");
        add(p, g, "System BGM", "DJMAX RESPECT V", "6:04");
        add(p, g, "Proof of a Hero", "Monster Hunter", "4:14");
        add(p, g, "Still Alive", "Portal", "3:05");
        add(p, g, "Loading", "Valorant", "3:32");
        add(p, g, "Everybody Falls", "Fall Guys", "3:18");
        add(p, g, "Baba Yetu", "Civilization", "3:35");
        add(p, g, "Not a Hero", "King's Raid", "2:27");
        add(p, g, "Main Theme", "To the Moon", "4:57");
        add(p, g, "Will the Circle Be Unbroken", "BioShock Infinite", "4:45");

        return p;
    }

    // Client view: main

    public static void main(String[] args) {
        PlaylistProofOfConcept mix = buildMyMix();

        System.out.println("== My mix: " + mix.length() + " songs, "
                + formatTime(mix.totalDuration()) + " total ==");
        System.out.print(mix.preview(5));

        System.out.println("\n== Breakdown by theme ==");
        for (Theme t : Theme.values()) {
            System.out.printf("  %-11s %2d songs, %s%n", t.label(),
                    mix.countOf(t), formatTime(mix.durationOf(t)));
        }

        System.out.println("\n== Lookup ==");
        System.out.println(
                "  contains \"Whiplash\"?  " + mix.containsTitle("Whiplash"));
        System.out.println(
                "  contains \"Nope\"?      " + mix.containsTitle("Nope"));

        System.out.println("\n== Skip the first song ==");
        mix.skip();
        System.out.print(mix.preview(3));

        System.out.println("\n== Jump to the first EDM song ==");
        mix.skipToTheme(Theme.EDM);
        System.out.print(mix.preview(3));

        System.out.println("\n== Reverse ==");
        mix.reverse();
        System.out.print(mix.preview(3));

        System.out.println("\n== Shuffle (seeded so output is repeatable) ==");
        mix.shuffle(new Random(42));
        System.out.print(mix.preview(5));

        System.out.println("\n== Split the Game OST songs into their own playlist ==");
        PlaylistProofOfConcept ost = mix.extractTheme(Theme.GAME_OST);
        System.out.println("Game OST playlist (" + ost.length() + " songs, "
                + formatTime(ost.totalDuration()) + "):");
        System.out.print(ost);
        System.out.println("Remaining mix: " + mix.length() + " songs");

        System.out.println("\n== Append the Game OST playlist back ==");
        mix.appendPlaylist(ost);
        System.out.println("  mix now has " + mix.length() + " songs, "
                + formatTime(mix.totalDuration()) + "; ost has "
                + ost.length() + " songs");
    }
}
