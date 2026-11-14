import java.lang.reflect.Constructor;

import components.queue.Queue;
import components.queue.Queue1L;

/**
 * {@code Playlist} kernel implementation on a {@code Queue<Song>}.
 *
 * @convention <pre>
 * $this.songs is not null  and
 * no element of $this.songs is null
 * </pre>
 * @correspondence <pre>
 * this = $this.songs  (the front is the next song to play)
 * </pre>
 */
public class Playlist1L extends PlaylistSecondary {

    // Private members

    // Songs in order
    private Queue<Song> songs;

    // Creator of initial representation
    private void createNewRep() {
        this.songs = new Queue1L<Song>();
    }

    // Constructors
    public Playlist1L() {
        this.createNewRep();
    }

    // Standard methods

    @Override
    public final Playlist newInstance() {
        try {
            Constructor<?> c = this.getClass().getConstructor();
            return (Playlist) c.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Playlist source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Playlist1L : ""
                + "Violation of: source is of dynamic type Playlist1L";
        // The cast cannot fail: the assert above guarantees the dynamic type
        Playlist1L localSource = (Playlist1L) source;
        this.songs = localSource.songs;
        localSource.createNewRep();
    }

    // Kernel methods

    @Override
    public final void addSong(Song s) {
        assert s != null : "Violation of: s is not null";

        this.songs.enqueue(s);
    }

    @Override
    public final Song removeFirst() {
        assert this.songs.length() > 0 : "Violation of: |this| > 0";

        return this.songs.dequeue();
    }

    @Override
    public final int length() {
        return this.songs.length();
    }

}