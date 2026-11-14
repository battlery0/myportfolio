import java.util.Random;

/** Use case 1: organizing a music library by theme. */
public final class PlaylistDemo {

        private PlaylistDemo() {
        }

        private static void add(Playlist p, PlaylistKernel.Theme theme,
                        String title, String artist, String time) {
                String[] parts = time.split(":");
                int seconds = Integer.parseInt(parts[0]) * 60
                                + Integer.parseInt(parts[1]);
                p.addSong(new Song(title, artist, seconds, theme));
        }

        private static String formatTime(int seconds) {
                return String.format("%d:%02d", seconds / 60, seconds % 60);
        }

        public static void main(String[] args) {
                PlaylistKernel.Theme kpop = PlaylistKernel.Theme.KPOP_IDOLS;
                PlaylistKernel.Theme bands = PlaylistKernel.Theme.K_BANDS;
                PlaylistKernel.Theme jpop = PlaylistKernel.Theme.JPOP;
                PlaylistKernel.Theme edm = PlaylistKernel.Theme.EDM;
                PlaylistKernel.Theme ost = PlaylistKernel.Theme.GAME_OST;

                // A mixed library, ordered by added
                Playlist library = new Playlist1L();
                add(library, kpop, "Attention", "NewJeans", "2:58");
                add(library, jpop, "kirari", "Fujii Kaze", "4:01");
                add(library, edm, "Dopamine", "Wings", "2:49");
                add(library, ost, "Still Alive", "Portal", "3:05");
                add(library, bands, "Surrender", "MRCH", "3:33");
                add(library, kpop, "Whiplash", "aespa", "3:11");
                add(library, edm, "Insight", "Haywyre", "3:55");
                add(library, jpop, "Dramaturgy", "Eve", "4:06");
                add(library, ost, "Baba Yetu", "Civilization", "3:35");
                add(library, bands, "Give Love", "AKMU", "3:50");
                add(library, kpop, "Magnetic", "ILLIT", "3:09");
                add(library, edm, "GHOST", "Camellia", "5:50");
                add(library, ost, "Main Theme", "To the Moon", "4:57");
                add(library, jpop, "Ikuoku Kounen", "Omoinotake", "5:16");
                add(library, bands, "Moon", "AKMU", "4:04");

                System.out.println("Library: " + library.length() + " songs, "
                                + formatTime(library.totalDuration()));

                // Split the library into one playlist per theme
                Playlist[] byTheme = new Playlist[PlaylistKernel.Theme
                                .values().length];
                System.out.println("\nSplit by theme:");
                for (PlaylistKernel.Theme theme : PlaylistKernel.Theme
                                .values()) {
                        Playlist themed = library.extractTheme(theme);
                        byTheme[theme.ordinal()] = themed;
                        System.out.printf("  %-10s %d songs, %s%n", theme,
                                        themed.length(),
                                        formatTime(themed.totalDuration()));
                }
                System.out.println("Library after splitting: "
                                + library.length() + " songs left");

                // Build a custom curated mix from two of the themes
                Playlist curated = new Playlist1L();
                curated.appendPlaylist(byTheme[kpop.ordinal()]);
                curated.appendPlaylist(byTheme[edm.ordinal()]);
                System.out.println("\nCurated mix (K-pop + EDM), in order:");
                System.out.println("  " + curated);

                curated.shuffle(new Random(2026));
                System.out.println("\nAfter shuffling:");
                System.out.println("  " + curated);

                curated.skipToTheme(kpop);
                System.out.println("\nJumped to the first K-pop song:");
                System.out.println("  " + curated);

                System.out.println("\nContains \"Whiplash\"? "
                                + curated.containsTitle("Whiplash"));
                System.out.println("Curated mix length: "
                                + formatTime(curated.totalDuration()));
        }

}