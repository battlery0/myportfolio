import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

/**
 * JUnit tests for the {@code Playlist} secondary methods (run with
 * {@code -ea}).
 */
public class PlaylistTest {

    private static final PlaylistKernel.Theme EDM = PlaylistKernel.Theme.EDM;
    private static final PlaylistKernel.Theme JPOP = PlaylistKernel.Theme.JPOP;
    private static final PlaylistKernel.Theme KPOP = PlaylistKernel.Theme.KPOP_IDOLS;
    private static final PlaylistKernel.Theme OST = PlaylistKernel.Theme.GAME_OST;

    private static Song song(String title, int seconds,
            PlaylistKernel.Theme theme) {
        return new Song(title, "Artist", seconds, theme);
    }

    // a, c = EDM; b = JPOP; d = KPOP
    private static final Song A = song("a", 10, EDM);
    private static final Song B = song("b", 20, JPOP);
    private static final Song C = song("c", 30, EDM);
    private static final Song D = song("d", 40, KPOP);

    private static Playlist playlistOf(Song... songs) {
        Playlist p = new Playlist1L();
        for (Song s : songs) {
            p.addSong(s);
        }
        return p;
    }

    private static String order(Playlist p) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = p.length(); i < n; i++) {
            Song s = p.removeFirst();
            sb.append(s.title());
            p.addSong(s);
        }
        return sb.toString();
    }

    // skip

    @Test
    public void testSkipEmpty() {
        Playlist p = playlistOf();
        p.skip();
        assertEquals("", order(p));
    }

    @Test
    public void testSkipOneSong() {
        Playlist p = playlistOf(A);
        p.skip();
        assertEquals("a", order(p));
    }

    @Test
    public void testSkipManySongs() {
        Playlist p = playlistOf(A, B, C, D);
        p.skip();
        assertEquals("bcda", order(p));
    }

    @Test
    public void testSkipLengthTimesRestoresOrder() {
        Playlist p = playlistOf(A, B, C, D);
        for (int i = 0; i < 4; i++) {
            p.skip();
        }
        assertEquals("abcd", order(p));
    }

    // shuffle

    @Test
    public void testShuffleEmpty() {
        Playlist p = playlistOf();
        p.shuffle(new Random(1));
        assertEquals("", order(p));
    }

    @Test
    public void testShuffleOneSong() {
        Playlist p = playlistOf(A);
        p.shuffle(new Random(1));
        assertEquals("a", order(p));
    }

    @Test
    public void testShuffleKeepsSameSongs() {
        for (long seed = 0; seed < 25; seed++) {
            Playlist p = playlistOf(A, B, C, D);
            p.shuffle(new Random(seed));
            String result = order(p);
            assertEquals(4, p.length());
            for (String title : new String[] { "a", "b", "c", "d" }) {
                assertTrue("missing " + title + " with seed " + seed,
                        result.contains(title));
            }
            assertEquals(100, p.totalDuration());
        }
    }

    @Test
    public void testShuffleSameSeedGivesSameOrder() {
        Playlist p1 = playlistOf(A, B, C, D);
        Playlist p2 = playlistOf(A, B, C, D);
        p1.shuffle(new Random(7));
        p2.shuffle(new Random(7));
        assertEquals(order(p1), order(p2));
    }

    @Test
    public void testShuffleCanChangeOrder() {
        boolean changed = false;
        for (long seed = 0; seed < 25 && !changed; seed++) {
            Playlist p = playlistOf(A, B, C, D);
            p.shuffle(new Random(seed));
            changed = !order(p).equals("abcd");
        }
        assertTrue(changed);
    }

    @Test(expected = AssertionError.class)
    public void testShuffleNullViolatesContract() {
        playlistOf(A).shuffle(null);
    }

    // reverse

    @Test
    public void testReverseEmpty() {
        Playlist p = playlistOf();
        p.reverse();
        assertEquals("", order(p));
    }

    @Test
    public void testReverseOneSong() {
        Playlist p = playlistOf(A);
        p.reverse();
        assertEquals("a", order(p));
    }

    @Test
    public void testReverseTwoSongs() {
        Playlist p = playlistOf(A, B);
        p.reverse();
        assertEquals("ba", order(p));
    }

    @Test
    public void testReverseManySongs() {
        Playlist p = playlistOf(A, B, C, D);
        p.reverse();
        assertEquals("dcba", order(p));
    }

    @Test
    public void testReverseTwiceRestoresOrder() {
        Playlist p = playlistOf(A, B, C, D);
        p.reverse();
        p.reverse();
        assertEquals("abcd", order(p));
    }

    // containsTitle

    @Test
    public void testContainsTitleEmpty() {
        assertFalse(playlistOf().containsTitle("a"));
    }

    @Test
    public void testContainsTitleFirstMiddleLast() {
        Playlist p = playlistOf(A, B, C);
        assertTrue(p.containsTitle("a"));
        assertTrue(p.containsTitle("b"));
        assertTrue(p.containsTitle("c"));
    }

    @Test
    public void testContainsTitleAbsent() {
        assertFalse(playlistOf(A, B, C).containsTitle("z"));
    }

    @Test
    public void testContainsTitleIsCaseSensitive() {
        assertFalse(playlistOf(A).containsTitle("A"));
    }

    @Test
    public void testContainsTitleWithDuplicateTitles() {
        assertTrue(playlistOf(A, A).containsTitle("a"));
    }

    @Test
    public void testContainsTitleDoesNotChangePlaylist() {
        Playlist p = playlistOf(A, B, C);
        p.containsTitle("b");
        assertEquals("abc", order(p));
    }

    @Test(expected = AssertionError.class)
    public void testContainsTitleNullViolatesContract() {
        playlistOf(A).containsTitle(null);
    }

    // totalDuration

    @Test
    public void testTotalDurationEmpty() {
        assertEquals(0, playlistOf().totalDuration());
    }

    @Test
    public void testTotalDurationOneSong() {
        assertEquals(10, playlistOf(A).totalDuration());
    }

    @Test
    public void testTotalDurationManySongs() {
        assertEquals(100, playlistOf(A, B, C, D).totalDuration());
    }

    @Test
    public void testTotalDurationCountsDuplicates() {
        assertEquals(20, playlistOf(A, A).totalDuration());
    }

    @Test
    public void testTotalDurationDoesNotChangePlaylist() {
        Playlist p = playlistOf(A, B, C);
        p.totalDuration();
        assertEquals("abc", order(p));
    }

    // appendPlaylist

    @Test
    public void testAppendEmptyToEmpty() {
        Playlist p = playlistOf();
        Playlist other = playlistOf();
        p.appendPlaylist(other);
        assertEquals("", order(p));
        assertEquals(0, other.length());
    }

    @Test
    public void testAppendManyToEmpty() {
        Playlist p = playlistOf();
        Playlist other = playlistOf(A, B);
        p.appendPlaylist(other);
        assertEquals("ab", order(p));
        assertEquals(0, other.length());
    }

    @Test
    public void testAppendEmptyToMany() {
        Playlist p = playlistOf(A, B);
        Playlist other = playlistOf();
        p.appendPlaylist(other);
        assertEquals("ab", order(p));
        assertEquals(0, other.length());
    }

    @Test
    public void testAppendManyToMany() {
        Playlist p = playlistOf(A, B);
        Playlist other = playlistOf(C, D);
        p.appendPlaylist(other);
        assertEquals("abcd", order(p));
        assertEquals(0, other.length());
    }

    @Test
    public void testAppendOtherIsUsableAfterwards() {
        Playlist p = playlistOf(A);
        Playlist other = playlistOf(B);
        p.appendPlaylist(other);
        other.addSong(C);
        assertEquals("c", order(other));
        assertEquals("ab", order(p));
    }

    @Test(expected = AssertionError.class)
    public void testAppendSelfViolatesContract() {
        Playlist p = playlistOf(A);
        p.appendPlaylist(p);
    }

    @Test(expected = AssertionError.class)
    public void testAppendNullViolatesContract() {
        playlistOf(A).appendPlaylist(null);
    }

    // countOf

    @Test
    public void testCountOfEmpty() {
        assertEquals(0, playlistOf().countOf(EDM));
    }

    @Test
    public void testCountOfNoMatch() {
        assertEquals(0, playlistOf(A, B, C, D).countOf(OST));
    }

    @Test
    public void testCountOfSomeMatch() {
        Playlist p = playlistOf(A, B, C, D);
        assertEquals(2, p.countOf(EDM));
        assertEquals(1, p.countOf(JPOP));
    }

    @Test
    public void testCountOfAllMatch() {
        assertEquals(2, playlistOf(A, C).countOf(EDM));
    }

    @Test
    public void testCountOfDoesNotChangePlaylist() {
        Playlist p = playlistOf(A, B, C, D);
        p.countOf(EDM);
        assertEquals("abcd", order(p));
    }

    @Test(expected = AssertionError.class)
    public void testCountOfNullViolatesContract() {
        playlistOf(A).countOf(null);
    }

    // durationOf

    @Test
    public void testDurationOfEmpty() {
        assertEquals(0, playlistOf().durationOf(EDM));
    }

    @Test
    public void testDurationOfNoMatch() {
        assertEquals(0, playlistOf(A, B, C, D).durationOf(OST));
    }

    @Test
    public void testDurationOfSomeMatch() {
        Playlist p = playlistOf(A, B, C, D);
        assertEquals(40, p.durationOf(EDM));
        assertEquals(20, p.durationOf(JPOP));
    }

    @Test
    public void testDurationOfAllMatch() {
        assertEquals(40, playlistOf(A, C).durationOf(EDM));
    }

    @Test
    public void testDurationOfDoesNotChangePlaylist() {
        Playlist p = playlistOf(A, B, C, D);
        p.durationOf(EDM);
        assertEquals("abcd", order(p));
    }

    @Test(expected = AssertionError.class)
    public void testDurationOfNullViolatesContract() {
        playlistOf(A).durationOf(null);
    }

    // extractTheme

    @Test
    public void testExtractThemeFromEmpty() {
        Playlist p = playlistOf();
        Playlist extracted = p.extractTheme(EDM);
        assertEquals("", order(extracted));
        assertEquals("", order(p));
    }

    @Test
    public void testExtractThemeNoMatch() {
        Playlist p = playlistOf(A, B, C, D);
        Playlist extracted = p.extractTheme(OST);
        assertEquals("", order(extracted));
        assertEquals("abcd", order(p));
    }

    @Test
    public void testExtractThemeSomeMatchKeepsOrder() {
        Playlist p = playlistOf(A, B, C, D);
        Playlist extracted = p.extractTheme(EDM);
        assertEquals("ac", order(extracted));
        assertEquals("bd", order(p));
    }

    @Test
    public void testExtractThemeAllMatch() {
        Playlist p = playlistOf(A, C);
        Playlist extracted = p.extractTheme(EDM);
        assertEquals("ac", order(extracted));
        assertEquals("", order(p));
    }

    @Test
    public void testExtractThemeReturnsSeparatePlaylist() {
        Playlist p = playlistOf(A, B);
        Playlist extracted = p.extractTheme(EDM);
        extracted.addSong(D);
        assertEquals("ad", order(extracted));
        assertEquals("b", order(p));
    }

    @Test(expected = AssertionError.class)
    public void testExtractThemeNullViolatesContract() {
        playlistOf(A).extractTheme(null);
    }

    // skipToTheme

    @Test
    public void testSkipToThemeEmpty() {
        Playlist p = playlistOf();
        p.skipToTheme(EDM);
        assertEquals("", order(p));
    }

    @Test
    public void testSkipToThemeAlreadyAtFront() {
        Playlist p = playlistOf(A, B, C, D);
        p.skipToTheme(EDM);
        assertEquals("abcd", order(p));
    }

    @Test
    public void testSkipToThemeInMiddle() {
        Playlist p = playlistOf(A, B, C, D);
        p.skipToTheme(JPOP);
        assertEquals("bcda", order(p));
    }

    @Test
    public void testSkipToThemeAtBack() {
        Playlist p = playlistOf(A, B, C, D);
        p.skipToTheme(KPOP);
        assertEquals("dabc", order(p));
    }

    @Test
    public void testSkipToThemeStopsAtFirstMatch() {
        Playlist p = playlistOf(B, A, D, C);
        p.skipToTheme(EDM);
        assertEquals("adcb", order(p));
    }

    @Test
    public void testSkipToThemeNotPresentChangesNothing() {
        Playlist p = playlistOf(A, B, C, D);
        p.skipToTheme(OST);
        assertEquals("abcd", order(p));
    }

    @Test(expected = AssertionError.class)
    public void testSkipToThemeNullViolatesContract() {
        playlistOf(A).skipToTheme(null);
    }

    // toString

    @Test
    public void testToStringEmpty() {
        assertEquals("<>", playlistOf().toString());
    }

    @Test
    public void testToStringOneSong() {
        assertEquals("<Artist - a>", playlistOf(A).toString());
    }

    @Test
    public void testToStringManySongs() {
        assertEquals("<Artist - a, Artist - b, Artist - c>",
                playlistOf(A, B, C).toString());
    }

    @Test
    public void testToStringDoesNotChangePlaylist() {
        Playlist p = playlistOf(A, B, C);
        p.toString();
        assertEquals("abc", order(p));
    }

    // equals

    @Test
    public void testEqualsSameObject() {
        Playlist p = playlistOf(A, B);
        assertTrue(p.equals(p));
    }

    @Test
    public void testEqualsTwoEmpty() {
        assertTrue(playlistOf().equals(playlistOf()));
    }

    @Test
    public void testEqualsSameSongsSameOrder() {
        Playlist p1 = playlistOf(A, B, C);
        Playlist p2 = playlistOf(A, B, C);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
    }

    @Test
    public void testEqualsComparesSongsByValue() {
        Playlist p1 = playlistOf(song("x", 5, OST));
        Playlist p2 = playlistOf(song("x", 5, OST));
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testNotEqualsDifferentOrder() {
        assertFalse(playlistOf(A, B).equals(playlistOf(B, A)));
    }

    @Test
    public void testNotEqualsDifferentLength() {
        assertFalse(playlistOf(A, B).equals(playlistOf(A)));
        assertFalse(playlistOf().equals(playlistOf(A)));
    }

    @Test
    public void testNotEqualsDifferentSongSameTitle() {
        Playlist p1 = playlistOf(song("x", 5, OST));
        Playlist p2 = playlistOf(song("x", 6, OST));
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testNotEqualsNull() {
        assertFalse(playlistOf(A).equals(null));
    }

    @Test
    public void testNotEqualsOtherType() {
        assertFalse(playlistOf(A).equals("a"));
    }

    @Test
    public void testEqualsDoesNotChangeEitherPlaylist() {
        Playlist p1 = playlistOf(A, B, C);
        Playlist p2 = playlistOf(A, B, D);
        p1.equals(p2);
        assertEquals("abc", order(p1));
        assertEquals("abd", order(p2));
    }

    // hashCode

    @Test
    public void testHashCodeEqualPlaylistsMatch() {
        assertEquals(playlistOf(A, B, C).hashCode(),
                playlistOf(A, B, C).hashCode());
    }

    @Test
    public void testHashCodeEmptyPlaylistsMatch() {
        assertEquals(playlistOf().hashCode(), playlistOf().hashCode());
    }

    @Test
    public void testHashCodeDependsOnOrder() {
        assertNotEquals(playlistOf(A, B).hashCode(),
                playlistOf(B, A).hashCode());
    }

    @Test
    public void testHashCodeDoesNotChangePlaylist() {
        Playlist p = playlistOf(A, B, C);
        p.hashCode();
        assertEquals("abc", order(p));
    }

}