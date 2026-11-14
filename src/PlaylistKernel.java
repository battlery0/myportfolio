import components.standard.Standard;

/**
 * Kernel interface for {@code Playlist}: songs are added to the back and
 * removed from the front.
 *
 * @mathmodel type PlaylistKernel is modeled by string of Song
 * @initially <pre>
 * ensures
 *  this = &lt;&gt;
 * </pre>
 */
public interface PlaylistKernel extends Standard<Playlist> {

    //themes a {@code Song} can belong to.
    enum Theme {
        KPOP_IDOLS, K_BANDS, JPOP, EDM, GAME_OST
    }

    /**
     * Adds {@code s} to the back of {@code this}.
     *
     * @param s
     *            the song to add
     * @updates this
     * @requires s is not null
     * @ensures this = #this * &lt;s&gt;
     */
    void addSong(Song s);

    /**
     * Removes and returns the song at the front of {@code this}.
     *
     * @return the song that was at the front of {@code this}
     * @updates this
     * @requires |this| &gt; 0
     * @ensures &lt;removeFirst&gt; * this = #this
     */
    Song removeFirst();

    /**
     * Reports the number of songs in {@code this}.
     *
     * @return the number of songs in {@code this}
     * @ensures length = |this|
     */
    int length();

}