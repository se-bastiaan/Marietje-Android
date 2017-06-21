package eu.se_bastiaan.marietje.test.common;

import java.util.ArrayList;
import java.util.List;

import eu.se_bastiaan.marietje.data.model.Permissions;
import eu.se_bastiaan.marietje.data.model.PlaylistSong;
import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.data.model.Songs;

/**
 * Factory class that makes instances of data models with random field values.
 * The aim of this class is to help setting up test fixtures.
 */
public class TestDataFactory {

    public static Song makeSong(long id) {
        return new Song(id, 100L * id, "title " + id, "artist " + id, "uploader " + id);
    }

    public static List<Song> makeListSongs(int number) {
        List<Song> songs = new ArrayList<>();
        for (int i = 1; i < number + 1; i++) {
            songs.add(makeSong(i));
        }
        return songs;
    }

    public static PlaylistSong makePlaylistSong(long id) {
        return new PlaylistSong(id, makeSong(id), id % 2 == 0, "requester " + id);
    }

    public static List<PlaylistSong> makeListPlaylistSongs(int number) {
        List<PlaylistSong> songs = new ArrayList<>();
        for (int i = 1; i < number + 1; i++) {
            songs.add(makePlaylistSong(i));
        }
        return songs;
    }

    public static Songs makeSongsResponse(long currentPage) {
        return new Songs(10, currentPage, 100, makeListSongs(10), 1000);
    }

    public static Songs makeEmptySongsResponse(long currentPage) {
        return new Songs(10, currentPage, currentPage, new ArrayList<>(), (currentPage - 1) * 10);
    }

    public static Queue makeQueueResponse() {
        return new Queue((System.currentTimeMillis() / 1000) - 90L,
                (System.currentTimeMillis() / 1000), makePlaylistSong(11), makeListPlaylistSongs(10));
    }

    public static Permissions makePermissionsResponse() {
        return new Permissions(false, true, false, true);
    }

    public static String makeNormalCsrfResponse() {
        return "";
    }

}