/**
 * Use case 2: a {@code Playlist} used inside another component.
 *
 * @convention <pre>
 * upcoming and played are not null and are different objects
 * </pre>
 * @correspondence <pre>
 * this = (upcoming, played)
 * </pre>
 */
public class ListeningSession {

    // Songs that have not been played yet
    private Playlist upcoming;

    // Songs that have been played
    private Playlist played;

    // Takes the songs of queue
    public ListeningSession(Playlist queue) {
        assert queue != null : "Violation of: queue is not null";

        // Take the songs instead of keeping a reference, to avoid aliasing
        this.upcoming = queue.newInstance();
        this.upcoming.transferFrom(queue);
        this.played = queue.newInstance();
    }

    // True if at least one song is left to play
    public boolean hasNext() {
        return this.upcoming.length() > 0;
    }

    // Plays the next song and returns it
    public Song playNext() {
        assert this.hasNext() : "Violation of: there is a song left to play";

        Song next = this.upcoming.removeFirst();
        this.played.addSong(next);
        return next;
    }

    // Plays whole songs until at least seconds have played or none are left;
    // returns the seconds played
    public int playFor(int seconds) {
        assert seconds >= 0 : "Violation of: seconds >= 0";

        int total = 0;
        while (total < seconds && this.hasNext()) {
            total += this.playNext().durationSeconds();
        }
        return total;
    }

    // Jumps to the first unplayed song with the theme
    public void skipToTheme(PlaylistKernel.Theme theme) {
        assert theme != null : "Violation of: theme is not null";

        this.upcoming.skipToTheme(theme);
    }

    // Puts all played songs back at the end of the upcoming
    public void repeatPlayed() {
        this.upcoming.appendPlaylist(this.played);
    }

    // Seconds left to play
    public int secondsRemaining() {
        return this.upcoming.totalDuration();
    }

    // Number of songs played
    public int songsPlayed() {
        return this.played.length();
    }

    @Override
    public String toString() {
        return "played " + this.played + " | up next " + this.upcoming;
    }

    private static void add(Playlist p, PlaylistKernel.Theme theme,
            String title, String artist, int seconds) {
        p.addSong(new Song(title, artist, seconds, theme));
    }

    public static void main(String[] args) {
        Playlist commute = new Playlist1L();
        add(commute, PlaylistKernel.Theme.KPOP_IDOLS, "Attention", "NewJeans",
                178);
        add(commute, PlaylistKernel.Theme.JPOP, "kirari", "Fujii Kaze", 241);
        add(commute, PlaylistKernel.Theme.EDM, "Dopamine", "Wings", 169);
        add(commute, PlaylistKernel.Theme.GAME_OST, "Still Alive", "Portal",
                185);
        add(commute, PlaylistKernel.Theme.EDM, "Insight", "Haywyre", 235);
        add(commute, PlaylistKernel.Theme.K_BANDS, "Give Love", "AKMU", 230);

        ListeningSession session = new ListeningSession(commute);
        System.out.println("Playlist passed in now has " + commute.length()
                + " songs (the session took them)");
        System.out.println(
                "Time to play: " + session.secondsRemaining() + " seconds");

        Song first = session.playNext();
        System.out
                .println("\nPlayed: " + first.artist() + " - " + first.title());

        session.skipToTheme(PlaylistKernel.Theme.EDM);
        System.out.println("Skipped ahead to EDM");

        int played = session.playFor(300);
        System.out.println("Listened for " + played + " seconds");
        System.out.println(session);

        System.out.println("Songs played so far: " + session.songsPlayed()
                + ", time left: " + session.secondsRemaining() + " seconds");

        session.repeatPlayed();
        System.out.println("\nRepeating what was played:");
        System.out.println(session);
        System.out.println("Time left after repeating: "
                + session.secondsRemaining() + " seconds");
    }

}