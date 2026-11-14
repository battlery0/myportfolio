import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit tests for {@code Playlist1L}: constructor, kernel, and Standard
 * methods.
 */
public class Playlist1LTest {

    private static Song song(String title, int seconds,
            PlaylistKernel.Theme theme) {
        return new Song(title, "Artist", seconds, theme);
    }

    private static final Song A = song("A", 100, PlaylistKernel.Theme.EDM);
    private static final Song B = song("B", 200, PlaylistKernel.Theme.JPOP);
    private static final Song C = song("C", 300,
            PlaylistKernel.Theme.KPOP_IDOLS);

    private static Playlist1L playlistOf(Song... songs) {
        Playlist1L p = new Playlist1L();
        for (Song s : songs) {
            p.addSong(s);
        }
        return p;
    }

    // Constructor

    @Test
    public void testConstructorCreatesEmptyPlaylist() {
        Playlist1L p = new Playlist1L();
        assertEquals(0, p.length());
    }

    // addSong

    @Test
    public void testAddSongToEmpty() {
        Playlist1L p = new Playlist1L();
        p.addSong(A);
        assertEquals(1, p.length());
        assertSame(A, p.removeFirst());
    }

    @Test
    public void testAddSongKeepsFirstInFirstOutOrder() {
        Playlist1L p = playlistOf(A, B, C);
        assertEquals(3, p.length());
        assertSame(A, p.removeFirst());
        assertSame(B, p.removeFirst());
        assertSame(C, p.removeFirst());
    }

    @Test
    public void testAddSongAllowsDuplicates() {
        Playlist1L p = playlistOf(A, A);
        assertEquals(2, p.length());
        assertSame(A, p.removeFirst());
        assertSame(A, p.removeFirst());
    }

    @Test(expected = AssertionError.class)
    public void testAddSongNullViolatesContract() {
        new Playlist1L().addSong(null);
    }

    // removeFirst

    @Test
    public void testRemoveFirstLeavesOneSong() {
        Playlist1L p = playlistOf(A);
        assertSame(A, p.removeFirst());
        assertEquals(0, p.length());
    }

    @Test
    public void testRemoveFirstFromManyKeepsRest() {
        Playlist1L p = playlistOf(A, B, C);
        assertSame(A, p.removeFirst());
        assertEquals(2, p.length());
        assertSame(B, p.removeFirst());
    }

    @Test
    public void testRemoveFirstThenAddGoesToBack() {
        Playlist1L p = playlistOf(A, B);
        p.removeFirst();
        p.addSong(C);
        assertSame(B, p.removeFirst());
        assertSame(C, p.removeFirst());
    }

    @Test(expected = AssertionError.class)
    public void testRemoveFirstOnEmptyViolatesContract() {
        new Playlist1L().removeFirst();
    }

    // length

    @Test
    public void testLengthTracksAddsAndRemoves() {
        Playlist1L p = new Playlist1L();
        assertEquals(0, p.length());
        p.addSong(A);
        assertEquals(1, p.length());
        p.addSong(B);
        assertEquals(2, p.length());
        p.removeFirst();
        assertEquals(1, p.length());
        p.removeFirst();
        assertEquals(0, p.length());
    }

    @Test
    public void testLengthDoesNotChangeThePlaylist() {
        Playlist1L p = playlistOf(A, B);
        assertEquals(2, p.length());
        assertEquals(2, p.length());
        assertSame(A, p.removeFirst());
        assertSame(B, p.removeFirst());
    }

    // newInstance

    @Test
    public void testNewInstanceIsEmptyAndSameType() {
        Playlist1L p = playlistOf(A, B);
        Playlist n = p.newInstance();
        assertTrue(n instanceof Playlist1L);
        assertEquals(0, n.length());
    }

    @Test
    public void testNewInstanceIsDifferentObjectAndLeavesOriginal() {
        Playlist1L p = playlistOf(A, B);
        Playlist n = p.newInstance();
        assertNotSame(p, n);
        assertEquals(2, p.length());
        assertSame(A, p.removeFirst());
    }

    @Test
    public void testNewInstanceFromEmpty() {
        Playlist n = new Playlist1L().newInstance();
        assertEquals(0, n.length());
    }

    // clear

    @Test
    public void testClearNonEmpty() {
        Playlist1L p = playlistOf(A, B, C);
        p.clear();
        assertEquals(0, p.length());
    }

    @Test
    public void testClearEmpty() {
        Playlist1L p = new Playlist1L();
        p.clear();
        assertEquals(0, p.length());
    }

    @Test
    public void testPlaylistIsUsableAfterClear() {
        Playlist1L p = playlistOf(A, B);
        p.clear();
        p.addSong(C);
        assertEquals(1, p.length());
        assertSame(C, p.removeFirst());
    }

    // transferFrom

    @Test
    public void testTransferFromMovesSongsAndEmptiesSource() {
        Playlist1L target = new Playlist1L();
        Playlist1L source = playlistOf(A, B, C);
        target.transferFrom(source);
        assertEquals(3, target.length());
        assertEquals(0, source.length());
        assertSame(A, target.removeFirst());
        assertSame(B, target.removeFirst());
        assertSame(C, target.removeFirst());
    }

    @Test
    public void testTransferFromReplacesOldContents() {
        Playlist1L target = playlistOf(C, C, C);
        Playlist1L source = playlistOf(A);
        target.transferFrom(source);
        assertEquals(1, target.length());
        assertSame(A, target.removeFirst());
    }

    @Test
    public void testTransferFromEmptySourceEmptiesTarget() {
        Playlist1L target = playlistOf(A, B);
        target.transferFrom(new Playlist1L());
        assertEquals(0, target.length());
    }

    @Test
    public void testSourceIsUsableAfterTransferFrom() {
        Playlist1L target = new Playlist1L();
        Playlist1L source = playlistOf(A, B);
        target.transferFrom(source);
        source.addSong(C);
        assertEquals(1, source.length());
        assertEquals(2, target.length());
        assertSame(C, source.removeFirst());
    }

    @Test(expected = AssertionError.class)
    public void testTransferFromSelfViolatesContract() {
        Playlist1L p = playlistOf(A);
        p.transferFrom(p);
    }

    @Test(expected = AssertionError.class)
    public void testTransferFromNullViolatesContract() {
        new Playlist1L().transferFrom(null);
    }

}