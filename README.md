# Playlist

Java component that models a music playlist, built with the OSU
software sequence discipline (kernel interface, enhanced interface, abstract
class, and kernel implementation).

A playlist is an ordered list of songs. Songs are added to the back and played
from the front. Each song has a title, an artist, a length in seconds, and a
theme: K-pop idols, K-bands, J-pop, EDM, or game soundtrack.


## Design

| File                               | Role                                                                                                      |
| ---------------------------------- | --------------------------------------------------------------------------------------------------------- |
| `src/PlaylistKernel.java`          | Minimal methods: `addSong`, `removeFirst`, `length`                                                       |
| `src/Playlist.java`                | `skip`, `shuffle`, `reverse`, `containsTitle`, `totalDuration`, `appendPlaylist`, `countOf`, `durationOf`, `extractTheme`, `skipToTheme` |
| `src/PlaylistSecondary.java`       | Implements the enhanced methods and `toString`, `equals`, `hashCode` using only kernel methods             |
| `src/Playlist1L.java`             | Represents a playlist as a `Queue<Song>`                                                                  |
| `src/Song.java`                    | Immutable song                                                                                            |

## Tests

- `Playlist1LTest.java`: constructor, kernel methods, and Standard methods
- `PlaylistTest.java`: secondary methods, `toString`, `equals`, and `hashCode`

## Requirements

Java 16 or newer (`Song` is a record) and the OSU CSE components library in
`lib/`.

## Changelog

See [CHANGELOG.md](CHANGELOG.md).