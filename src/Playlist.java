import java.util.Random;

/**
 * Enhanced interface for {@code Playlist}. Every method here can be
 * implemented using only the kernel methods of {@link PlaylistKernel} and the
 * {@code Standard} methods ({@code clear}, {@code newInstance},
 * {@code transferFrom}).
 *
 */
public interface Playlist extends PlaylistKernel {

    /**
     * Moves the front song of {@code this} to the back. If {@code this} is
     * empty, nothing changes.
     *
     * @updates this
     * @ensures <pre>
     * if |#this| = 0 then
     *  this = #this
     * else
     *  this = #this[1, |#this|) * #this[0, 1)
     * </pre>
     */
    void skip();

    /**
     * Randomly reorders the songs in {@code this}.
     *
     * @param rng
     *            the source of randomness used to pick the new order
     * @updates this, rng
     * @requires rng is not null
     * @ensures this is a permutation of #this
     */
    void shuffle(Random rng);

    /**
     * Reverses the order of the songs in {@code this}.
     *
     * @updates this
     * @ensures this = reverse(#this)
     */
    void reverse();

    /**
     * Reports whether {@code this} contains a song with the given title.
     *
     * @param title
     *            the title to look for
     * @return true iff some song in {@code this} has title {@code title}
     * @requires title is not null
     * @ensures <pre>
     * containsTitle = (there exists i: integer
     *     where (0 &lt;= i &lt; |this| and this[i].title = title))
     * </pre>
     */
    boolean containsTitle(String title);

    /**
     * Reports the total length of all songs in {@code this}.
     *
     * @return the sum of the durations of the songs in {@code this}, in
     *         seconds
     * @ensures totalDuration = sum of this[i].durationSeconds for 0 &lt;= i &lt;
     *          |this|
     */
    int totalDuration();

    /**
     * Moves all songs of {@code other} onto the back of {@code this}.
     *
     * @param other
     *            the playlist whose songs are moved
     * @updates this
     * @clears other
     * @requires other is not this
     * @ensures this = #this * #other
     */
    void appendPlaylist(Playlist other);

    /**
     * Reports how many songs in {@code this} have the given theme.
     *
     * @param theme
     *            the theme to count
     * @return the number of songs in {@code this} whose theme is
     *         {@code theme}
     * @requires theme is not null
     * @ensures countOf = |songs of this whose theme is theme|
     */
    int countOf(Theme theme);

    /**
     * Reports the total length of the songs in {@code this} that have the
     * given theme.
     *
     * @param theme
     *            the theme to total
     * @return the sum of the durations (in seconds) of the songs in
     *         {@code this} whose theme is {@code theme}
     * @requires theme is not null
     * @ensures durationOf = sum of the durations of songs of this whose
     *          theme is theme
     */
    int durationOf(Theme theme);

    /**
     * Removes every song with the given theme from {@code this} and returns
     * them, in their original order, as a new playlist.
     *
     * @param theme
     *            the theme to extract
     * @return a playlist holding the songs that were removed
     * @updates this
     * @requires theme is not null
     * @ensures <pre>
     * extractTheme = the songs of #this whose theme is theme, in order  and
     * this = the songs of #this whose theme is not theme, in order
     * </pre>
     */
    Playlist extractTheme(Theme theme);

    /**
     * Skips songs until the front song of {@code this} has the given theme. If
     * no song has that theme, nothing changes. The skipped songs go to the back
     * in their original order.
     *
     * @param theme
     *            the theme to jump to
     * @updates this
     * @requires theme is not null
     * @ensures <pre>
     * if countOf(theme) = 0 then
     *  this = #this
     * else
     *  there exists i: integer
     *      where (this[0].theme = theme  and
     *             for all j: integer where (0 &lt;= j &lt; i) (#this[j].theme /= theme)  and
     *             this = #this[i, |#this|) * #this[0, i))
     * </pre>
     */
    void skipToTheme(Theme theme);

}