/**
 * An immutable song in a {@code Playlist}.
 *
 * @param title
 *            the title of the song (never null)
 * @param artist
 *            the artist of the song (never null)
 * @param durationSeconds
 *            the length of the song in seconds (always positive)
 * @param theme
 *            the theme the song belongs to (never null)
 */
public record Song(String title, String artist, int durationSeconds,
        PlaylistKernel.Theme theme) {

    /**
     * Checks that every component of a new {@code Song} is valid.
     *
     * @throws IllegalArgumentException
     *             if {@code title}, {@code artist}, or {@code theme} is null,
     *             or if {@code durationSeconds} is not positive
     */
    public Song {
        if (title == null || artist == null || theme == null) {
            throw new IllegalArgumentException("Song fields cannot be null");
        }
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }
}