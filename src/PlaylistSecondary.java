import java.util.Random;

/**
 * Enhanced interface for {@code Playlist}, implementable using only kernel
 * and Standard methods.
 */
public abstract class PlaylistSecondary implements Playlist {


    private void rotate() {
        assert this.length() > 0 : "Violation of: |this| > 0";
        this.addSong(this.removeFirst());
    }


    @Override
    public final void skip() {
        if (this.length() > 0) {
            this.rotate();
        }
    }

    @Override
    public final void shuffle(Random rng) {
        assert rng != null : "Violation of: rng is not null";


        Playlist result = this.newInstance();
        int remaining = this.length();
        while (remaining > 0) {
            int pick = rng.nextInt(remaining);
            for (int i = 0; i < pick; i++) {
                this.rotate();
            }
            result.addSong(this.removeFirst());
            remaining--;
        }
        this.transferFrom(result);
    }

    @Override
    public final void reverse() {
        Playlist result = this.newInstance();
        for (int k = this.length(); k > 0; k--) {
            for (int i = 0; i < k - 1; i++) {
                this.rotate();
            }
            result.addSong(this.removeFirst());
        }
        this.transferFrom(result);
    }

    @Override
    public final boolean containsTitle(String title) {
        assert title != null : "Violation of: title is not null";

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

    @Override
    public final int totalDuration() {
        int total = 0;
        for (int i = 0, n = this.length(); i < n; i++) {
            Song s = this.removeFirst();
            total += s.durationSeconds();
            this.addSong(s);
        }
        return total;
    }

    @Override
    public final void appendPlaylist(Playlist other) {
        assert other != null : "Violation of: other is not null";
        assert other != this : "Violation of: other is not this";

        while (other.length() > 0) {
            this.addSong(other.removeFirst());
        }
    }

    @Override
    public final int countOf(Theme theme) {
        assert theme != null : "Violation of: theme is not null";

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

    @Override
    public final int durationOf(Theme theme) {
        assert theme != null : "Violation of: theme is not null";

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

    @Override
    public final Playlist extractTheme(Theme theme) {
        assert theme != null : "Violation of: theme is not null";

        Playlist extracted = this.newInstance();
        for (int i = 0, n = this.length(); i < n; i++) {
            Song s = this.removeFirst();
            if (s.theme() == theme) {
                extracted.addSong(s);
            } else {
                this.addSong(s);
            }
        }
        return extracted;
    }

    @Override
    public final void skipToTheme(Theme theme) {
        assert theme != null : "Violation of: theme is not null";

        int n = this.length();
        int index = n;
        for (int i = 0; i < n; i++) {
            Song s = this.removeFirst();
            if (index == n && s.theme() == theme) {
                index = i;
            }
            this.addSong(s);
        }
        for (int i = 0; i < index; i++) {
            this.rotate();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<");
        for (int i = 0, n = this.length(); i < n; i++) {
            Song s = this.removeFirst();
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(s.artist()).append(" - ").append(s.title());
            this.addSong(s);
        }
        sb.append(">");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Playlist)) {
            return false;
        }
        Playlist other = (Playlist) obj;
        if (this.length() != other.length()) {
            return false;
        }
        boolean same = true;
        for (int i = 0, n = this.length(); i < n; i++) {
            Song mine = this.removeFirst();
            Song theirs = other.removeFirst();
            if (!mine.equals(theirs)) {
                same = false;
            }
            this.addSong(mine);
            other.addSong(theirs);
        }
        return same;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (int i = 0, n = this.length(); i < n; i++) {
            Song s = this.removeFirst();
            result = prime * result + s.hashCode();
            this.addSong(s);
        }
        return result;
    }

}