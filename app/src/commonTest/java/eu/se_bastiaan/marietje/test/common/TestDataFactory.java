package eu.se_bastiaan.marietje.test.common;

import java.util.ArrayList;
import java.util.List;

import eu.se_bastiaan.marietje.data.model.PlaylistSong;
import eu.se_bastiaan.marietje.data.model.QueueResponse;
import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.data.model.SongsResponse;

/**
 * Factory class that makes instances of data models with random field values.
 * The aim of this class is to help setting up test fixtures.
 */
public class TestDataFactory {

    public static Song makeSong(long id) {
        return Song.builder()
                .objectId(id)
                .title("title " + id)
                .artist("artist " + id)
                .duration(10000L + id * 100L)
                .uploader("uploader " + id)
                .build();
    }

    public static List<Song> makeListSongs(int number) {
        List<Song> songs = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            songs.add(makeSong(i));
        }
        return songs;
    }

    public static PlaylistSong makePlaylistSong(long id) {
        return PlaylistSong.builder()
                .objectId(id)
                .canMoveDown(id % 3 == 0)
                .requester("requester " + id)
                .song(makeSong(id))
                .build();
    }

    public static List<PlaylistSong> makeListPlaylistSongs(int number) {
        List<PlaylistSong> songs = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            songs.add(makePlaylistSong(i));
        }
        return songs;
    }

    public static SongsResponse makeSongsResponse(long currentPage) {
        return SongsResponse.builder()
                .currentPage(currentPage)
                .lastPage(100)
                .pageSize(10)
                .data(makeListSongs(10))
                .total(1000)
                .build();
    }

    public static SongsResponse makeEmptySongsResponse(long currentPage) {
        return SongsResponse.builder()
                .currentPage(currentPage)
                .lastPage(currentPage - 1)
                .pageSize(10)
                .data(new ArrayList<>())
                .total((currentPage - 1) * 10)
                .build();
    }

    public static QueueResponse makeQueueResponse() {
        return QueueResponse.builder()
                .currentSong(makePlaylistSong(100))
                .currentTime(System.currentTimeMillis())
                .startedAt(System.currentTimeMillis() - 1000L)
                .queue(makeListPlaylistSongs(10))
                .build();
    }

}